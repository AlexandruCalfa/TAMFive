package org.app.patterns;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.app.service.entities.EntityBase;
import org.jboss.logging.Logger;

public class EntityManagementBase<T extends EntityBase> implements EntityManagement<T> {
	private Logger logger = Logger.getLogger(EntityManagementBase.class);

	@PersistenceContext(unitName = "MSD")
	protected EntityManager entityManager;

	@Override
	public boolean createEntity(T entity) {
		entityManager.persist(entity);
		entityManager.flush();
		return true;
	}

	@Override
	public T readEntity(Class<T> entityClass, String id) {
		return entityManager.find(entityClass, id);
	}

	@Override
	public Collection<T> readAllEntities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateEntity(T entity) {
		if (entityManager.find(entity.getClass(), entity.getId()) == null) {
			return false;
		}
		entityManager.merge(entity);
		entityManager.flush();
		return true;
	}

	@Override
	public boolean deleteEntity(Class<T> entityClass, String id) {
		T entity;
		if ((entity = entityManager.find(entityClass, id)) == null) {
			return false;
		}

		logger.info(String.format("Trying to remove Benefit with id: ", entity.getId()));
		entityManager.remove(entity);
		entityManager.flush();
		return true;
	}

	@Override
	public boolean deleteAllEntities() {
		// TODO Auto-generated method stub
		return false;
	}

}
