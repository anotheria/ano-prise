package net.anotheria.anoprise.fs;

/**
 * Interface of the file system service.
 * 
 * @author abolbat
 * @version 1.0, 2010/02/11
 */
public interface FSService<T extends FSSaveable> {

	/**
	 * Returns a saved instance of T.
	 * 
	 * @param ownerId
	 *            - owner id
	 * @return instance of T
	 * @throws FSServiceException
	 * @throws FSItemNotFoundException
	 */
	T read(String ownerId) throws FSServiceException;

	/**
	 * Saves an instance of T.
	 * 
	 * @param t
	 *            - instance of T
	 * @throws FSServiceException
	 */
	void save(T t) throws FSServiceException;

	/**
	 * Delete an instance of T.
	 * 
	 * @param t
	 *            - instance of T
	 * @throws FSServiceException
	 */
	void delete(String ownerId) throws FSServiceException;
}
