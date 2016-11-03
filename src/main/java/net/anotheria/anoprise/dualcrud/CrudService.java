package net.anotheria.anoprise.dualcrud;

import java.util.List;

import net.anotheria.anoprise.metafactory.Service;

public interface CrudService<T extends CrudSaveable> extends Service {
	/**
	 * Creates a new saved instance of T. Disallows overwriting.
	 * 
	 * @param t
	 * @return created instance of T
	 * @throws CrudServiceException
	 */
	T create(T t) throws CrudServiceException;

	/**
	 * Returns a saved instance of T.
	 * 
	 * @param id
	 * @return instance of T
	 * @throws CrudServiceException
	 * @throws ItemNotFoundException
	 */
	T read(SaveableID id) throws CrudServiceException, ItemNotFoundException;

	/**
	 * Updates an existing instance of T. Fails if there is no previously saved instance.
	 * 
	 * @param t
	 * @return updated instance of T
	 * @throws CrudServiceException
	 */
	T update(T t) throws CrudServiceException;

	/**
	 * Deletes a saved instance of T. Should ignore non existing files.
	 * 
	 * @param t
	 * @throws CrudServiceException
	 */
	void delete(T t) throws CrudServiceException;

	/**
	 * Saves an instance of T, regardless, whether its an update or new creation.
	 * 
	 * @param t
	 * @return saved instance of T
	 * @throws CrudServiceException
	 */
	T save(T t) throws CrudServiceException;

	/**
	 * Returns true if the corresponding instance exists.
	 * 
	 * @param t
	 *            the instance to check.
	 * @return
	 * @throws CrudServiceException
	 */
	boolean exists(T t) throws CrudServiceException;

	/**
	 * Make query to service.
	 * 
	 * @param q
	 *            - {@link Query}
	 * @return {@link List} of T
	 * @throws CrudServiceException
	 */
	List<T> query(Query q) throws CrudServiceException;

}
