package net.anotheria.anoprise.dualcrud;

/**
 * This interface defines the dual crud service, a service that allows to work with two persistences in different mods.
 * @author lrosenberg
 *
 */
public interface DualCrudService<T extends CrudSaveable> {
	/**
	 * Creates a new instance.
	 */
	T create(T t) throws CrudServiceException;
	/**
	 * Reads an existing instance.
	 */
	T read(String ownerId) throws CrudServiceException;
	/**
	 * Updates an existing instance.
	 */
	T update(T t) throws CrudServiceException;
	/**
	 * Deletes an existing instance.
	 */
	void delete(T t) throws CrudServiceException;
	/**
	 * Saves an instance. This is similar to exists?update:create.
	 */
	T save(T t) throws CrudServiceException;
	/**
	 * Migrates an instance with given id from one persistence to another along the config.
	 */
	void migrate(String ownerId) throws CrudServiceException;
	/**
	 * Returns true if such an instance exists.
	 */
	boolean exists(T t) throws CrudServiceException;

	/**
	 * Everything else ;-). 
	 */
	QueryResult<T> query(Query q) throws CrudServiceException;

}
