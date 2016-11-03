package net.anotheria.anoprise.dualcrud;

/**
 * This interface defines the dual crud service, a service that allows to work with two persistences in different mods.
 * @author lrosenberg
 *
 * @param <T>
 */
public interface DualCrudService<T extends CrudSaveable> {
	/**
	 * Creates a new instance.
	 * @param t
	 * @return
	 * @throws CrudServiceException
	 */
	T create(T t) throws CrudServiceException;
	/**
	 * Reads an existing instance.
	 * @param id
	 * @return
	 * @throws CrudServiceException
	 */
	T read(SaveableID id) throws CrudServiceException;
	/**
	 * Updates an existing instance.
	 * @param t
	 * @return
	 * @throws CrudServiceException
	 */
	T update(T t) throws CrudServiceException;
	/**
	 * Deletes an existing instance.
	 * @param t
	 * @throws CrudServiceException
	 */
	void delete(T t) throws CrudServiceException;
	/**
	 * Saves an instance. This is similar to exists?update:create.
	 * @param t
	 * @return
	 * @throws CrudServiceException
	 */
	T save(T t) throws CrudServiceException;
	/**
	 * Migrates an instance with given id from one persistence to another along the config.
	 * @param id
	 * @throws CrudServiceException
	 */
	void migrate(SaveableID id) throws CrudServiceException;
	/**
	 * Returns true if such an instance exists.
	 * @param t
	 * @return
	 * @throws CrudServiceException
	 */
	boolean exists(T t) throws CrudServiceException;

	/**
	 * Everything else ;-). 
	 * @param q
	 * @return
	 * @throws CrudServiceException
	 */
	QueryResult<T> query(Query q) throws CrudServiceException;

}
