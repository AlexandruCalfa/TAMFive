package org.app.patterns;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.app.service.entities.Benefit;
import org.jboss.arquillian.transaction.api.annotation.Transactional;

public class EntityRepositoryBase<T extends Object> implements EntityRepository<T> {

	private Logger logger = Logger.getLogger(this.getClass().getName());

	@PersistenceContext(unitName = "MSD")
	protected EntityManager entityManager;

	protected Class<T> repositoryType;
	protected String genericSQL;

	@Override
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public EntityRepositoryBase() {

		logger.info("START DEFAULT INIT: ENTITY REPOSITORY ... ");

		this.repositoryType = getEntityParametrizedType();
		logger.info("init repositoryType: " + repositoryType.getSimpleName());

		this.genericSQL = "SELECT o FROM "
				+ repositoryType.getName().substring(repositoryType.getName().lastIndexOf('.') + 1) + " o";
		logger.info("init generic JPAQL: " + genericSQL);

		logger.info("... END DEFAULT INIT: ENTITY REPOSITORY!");
	}

	public EntityRepositoryBase(EntityManager em, Class<T> t) {
		this.entityManager = em;
		this.repositoryType = t;
		genericSQL = "SELECT o FROM "
				+ repositoryType.getName().substring(repositoryType.getName().lastIndexOf('.') + 1) + " o";
		logger.info("generic JPAQL: " + genericSQL);
	}

	public EntityRepositoryBase(Class<T> t) {
		this.repositoryType = t;
		genericSQL = "SELECT o FROM "
				+ repositoryType.getName().substring(repositoryType.getName().lastIndexOf('.') + 1) + " o";
		logger.info("generic JPAQL: " + genericSQL);
	}

	// Repository query implementation
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.app.patterns.EntityRepositoryService#getById(java.lang.Object)
	 */
	@Override
	public T getById(Object id) {
		return (T) entityManager.find(repositoryType, id);
	}

	// QBExample
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.app.patterns.EntityRepositoryService#get(T)
	 */
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Collection<T> get(T entitySample) {

		Map<String, Object> sqlCriterias = new HashMap<String, Object>();
		try {
			// get all properties and transform them into sqlCriterias
			PropertyDescriptor[] properties = Introspector.getBeanInfo(repositoryType).getPropertyDescriptors();
			Object propertyValue;
			Method readMethod;
			for (PropertyDescriptor property : properties) {
				readMethod = property.getReadMethod();
				if (readMethod != null) {
					logger.info("readMethod = " + readMethod);
					propertyValue = readMethod.invoke(entitySample);
					logger.info("propertyValue = " + propertyValue);
					if (propertyValue == null || property.getName().equals("class")) {
						continue;
					}
					if (propertyValue instanceof Collection && ((Collection) propertyValue).size() == 0) {
						continue;
					}
					sqlCriterias.put(property.getName(), propertyValue);
				}
			}
		} catch (IllegalAccessException ex) {
			Logger.getLogger(EntityRepositoryBase.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IllegalArgumentException ex) {
			Logger.getLogger(EntityRepositoryBase.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InvocationTargetException ex) {
			Logger.getLogger(EntityRepositoryBase.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IntrospectionException ex) {
			Logger.getLogger(EntityRepositoryBase.class.getName()).log(Level.SEVERE, null, ex);
		}

		if (sqlCriterias.isEmpty()) {
			return null;
		}

		String queryString = genericSQL + " WHERE ";
		for (String criteria : sqlCriterias.keySet()) {
			if (sqlCriterias.get(criteria) instanceof Collection) {
				queryString += "o." + criteria + " IN (:" + criteria + ") AND ";
			} else {
				queryString += "o." + criteria + " = :" + criteria + " AND ";
			}
		}
		queryString += " 1 = 1";

		logger.info("JPAQL: " + queryString);

		Query query = entityManager.createQuery(queryString);
		for (String criteria : sqlCriterias.keySet()) {
			query = query.setParameter(criteria, sqlCriterias.get(criteria));
		}
		return query.getResultList();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.app.patterns.EntityRepositoryService#toCollection()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Collection<T> toCollection() {
		logger.info("JPAQL: " + genericSQL);

		return entityManager.createQuery(genericSQL).getResultList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.app.patterns.EntityRepositoryService#toArray()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public T[] toArray() {
		logger.info("JPAQL: " + genericSQL);

		List<T> entities = entityManager.createQuery(genericSQL).getResultList();
		if (entities == null) {
			return null;
		}

		return (T[]) entities.toArray();
	}

	// Repository transaction implementation
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.app.patterns.EntityRepositoryService#add(T)
	 */
	@Override
	public T add(T entity) {
		// em.getTransaction().begin();
		try {
			// provideUri(entity);
			entityManager.merge(entity);
			// entityManager.getTransaction().commit();
			return entity;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Error while trying to insert entity in database: " + e.getClass().getName());
			// em.getTransaction().rollback();
			return null;
		} finally {
			// em.close();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.app.patterns.EntityRepositoryService#addAll(java.util.Collection)
	 */
	@Override
	public Collection<T> addAll(Collection<T> entities) {
		// em.getTransaction().begin();
		try {
			for (T entity : entities) {
				// provideUri(entity);
				entityManager.merge(entity);
			}
			// em.getTransaction().commit();
			return entities;
		} catch (Exception e) {
			e.printStackTrace();
			// em.getTransaction().rollback();
			return null;
		}
	}	

	@Transactional
	@Override
	public boolean remove(String entityId) {
		try {
			logger.info("Searching for entity with id: " + entityId);
			T entity = entityManager.find(repositoryType, entityId);
			logger.info("Found: " + entity);
			logger.info("remove this: merged." + entity+ ((Benefit) entity).getId());
			entityManager.remove(entity);
			logger.info("remove this: removed.");
			entityManager.flush();
			logger.info("remove this: flushed.");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.app.patterns.EntityRepositoryService#removeAll(java.util.Collection)
	 */
	@Override
	public boolean removeAll(Collection<T> entities) {
		// em.getTransaction().begin();
		try {
			for (Object c : entities) {
				entityManager.remove(c);
			}
			// em.getTransaction().commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			// em.getTransaction().rollback();
			return false;
		}
	}

	// Others
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.app.patterns.EntityRepositoryService#size()
	 */
	@Override
	public int size() {
		String sqlCount = "SELECT count(o) FROM "
				+ repositoryType.getName().substring(repositoryType.getName().lastIndexOf('.') + 1) + " o";

		logger.info("JPAQL: " + sqlCount);

		Long size = (Long) entityManager.createQuery(sqlCount).getSingleResult();
		return size.intValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.app.patterns.EntityRepositoryService#refresh(T)
	 */
	@Override
	public T refresh(T entity) {
		entity = entityManager.merge(entity);
		entityManager.refresh(entity);
		return entity;
	}

	private Class<?> extractClassFromType(Type t) throws ClassCastException {
		if (t instanceof Class<?>) {
			return (Class<?>) t;
		}
		return (Class<?>) ((ParameterizedType) t).getRawType();
	}

	public Class<T> getEntityParametrizedType() throws ClassCastException {
		Class<?> superClass = getClass(); // initial value
		Type superType;
		do {
			superType = superClass.getGenericSuperclass();
			superClass = extractClassFromType(superType);
		} while (!(superClass.equals(EntityRepositoryBase.class)));

		Type actualArg = ((ParameterizedType) superType).getActualTypeArguments()[0];
		return (Class<T>) extractClassFromType(actualArg);
	}
}