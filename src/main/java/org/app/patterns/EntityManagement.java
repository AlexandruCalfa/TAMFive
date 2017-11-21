package org.app.patterns;

import java.util.Collection;

import org.app.service.entities.EntityBase;

public interface EntityManagement<T extends EntityBase>{
		
	public boolean createEntity(T entity);

	public T readEntity(Class<T> entityClass, String id);

	public Collection<T> readAllEntities();
	
	public boolean updateEntity(T entity);
	
	public boolean deleteEntity(Class<T> entityClass, String id);
	
	public boolean deleteAllEntities();
}
