package net.anotheria.anoprise.fs;

/**
 * Interface of the file system service.
 * 
 * @author abolbat
 * @version 1.0, 2010/02/11
 */
public interface FSService<T extends FSSaveable> {

	/**
	 * Saves an instance of T.
	 * 
	 * @param t
	 *            - instance of T
	 * @return instance of T
	 * @throws FSServiceException
	 */
	T save(T t) throws FSServiceException;

	/**
	 * Returns a saved instance of T.
	 * 
	 * @param ownerId
	 *            - owner id
	 * @return instance of T
	 * @throws FSServiceException
	 * @throws FSItemNotFoundException
	 */
	T read(String ownerId) throws FSServiceException, FSItemNotFoundException;

}
