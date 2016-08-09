package net.anotheria.anoprise.fs;

/**
 * Interface of the file system service.
 * 
 * @author abolbat
 * @version 1.0, 2010/02/11
 * @param <T>
 *            - {@link FSSaveable} object type
 */
public interface FSService<T extends FSSaveable> {

	/**
	 * Returns a saved instance of T.
	 * 
	 * @param ownerId
	 *            - owner id
	 * @return instance of T
	 */
	T read(String ownerId) throws FSServiceException;

	/**
	 * Saves an instance of T.
	 * 
	 * @param t
	 *            - instance of T
	 */
	void save(T t) throws FSServiceException;

	/**
	 * Delete an instance of T.
	 * 
	 * @param ownerId
	 *            - owner id
	 */
	void delete(String ownerId) throws FSServiceException;
}
