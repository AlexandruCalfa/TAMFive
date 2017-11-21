package org.app.service.ejb;

import javax.ejb.Remote;
import javax.ws.rs.core.Response;

import org.app.patterns.EntityManagement;
import org.app.service.entities.EntityBase;

@Remote
public interface ServiceEJB<T extends EntityBase> extends EntityManagement<T> {

	/**
	 * Creates an object in the persistence layer.
	 * 
	 * @param parameter
	 *            contains information about the object to be created.
	 * @return the message.
	 */
	Response createObject(String parameter);

	/**
	 * Returns an object based on the provided id.
	 * 
	 * @param parameter
	 *            the id of the object.
	 * @return the object with the specified id.
	 */
	T readObject(String parameter);

	/**
	 * Updates an object with the fields provided.
	 * 
	 * @param parameter
	 *            information to be updated. Contains the id of the object.
	 * @return the message.
	 */
	Response updateObject(String parameter);

	/**
	 * Deletes an object based on the provided id.
	 * 
	 * @param parameter
	 *            the id of the object to be deleted.
	 * @return the message.
	 */
	Response deleteObject(String parameter);
}
