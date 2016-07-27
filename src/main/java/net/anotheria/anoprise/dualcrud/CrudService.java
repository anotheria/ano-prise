package net.anotheria.anoprise.dualcrud;

import net.anotheria.anoprise.metafactory.Service;

import java.util.List;

public interface CrudService<T extends CrudSaveable> extends Service {
	/**
	 * Creates a new saved instance of T. Disallows overwriting.
	 * 
	 * @return created instance of T
	 */
	T create(T t) throws CrudServiceException;

	/**
	 * Returns a saved instance of T.
	 * 
	 * @return instance of T
	 */
	T read(String ownerId) throws CrudServiceException;

	/**
	 * Updates an existing instance of T. Fails if there is no previously saved instance.
	 * 
	 * @return updated instance of T
	 */
	T update(T t) throws CrudServiceException;

	/**
	 * Deletes a saved instance of T. Should ignore non existing files.
	 * 
	 */
	void delete(T t);

	/**
	 * Saves an instance of T, regardless, whether its an update or new creation.
	 * 
	 * @return saved instance of T
	 */
	T save(T t) throws CrudServiceException;

	/**
	 * Returns true if the corresponding instance exists.
	 * 
	 * @param t
	 *            the instance to check.
	 */
	boolean exists(T t);

	/**
	 * Make query to service.
	 * 
	 * @param q
	 *            - {@link Query}
	 * @return {@link List} of T
	 */
	List<T> query(Query q);

}
