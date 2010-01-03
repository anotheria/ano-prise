package net.anotheria.anoprise.sessiondistributor;

import java.util.List;

import net.anotheria.anoprise.metafactory.Service;

/**
 * SessionDestributorService interface.
 * 
 * @author lrosenberg
 * @version 1.0, 2010/01/03
 */
public interface SessionDistributorService extends Service {

	/**
	 * Create new distributed session.
	 * 
	 * @param attributes
	 *            - attributes
	 * @return {@link String} session name
	 * @throws SessionDistributorServiceException
	 */
	String createDistributedSession(List<SessionAttribute> attributes) throws SessionDistributorServiceException;

	/**
	 * Delete distributed session.
	 * 
	 * @param name
	 *            - session name
	 * @throws SessionDistributorServiceException
	 */
	void deleteDistributedSession(String name) throws SessionDistributorServiceException;

	/**
	 * Get distributed session.
	 * 
	 * @param name
	 *            - session name
	 * @return {@link List} session attributes
	 * @throws SessionDistributorServiceException
	 */
	List<SessionAttribute> getDistributedSession(String name) throws SessionDistributorServiceException;

	/**
	 * Get and delete distributed session.
	 * 
	 * @param name
	 *            - session name
	 * @return {@link List} session attributes
	 * @throws SessionDistributorServiceException
	 */
	List<SessionAttribute> getAndDeleteDistributedSession(String name) throws SessionDistributorServiceException;

	/**
	 * Get distributed session names.
	 * 
	 * @return {@link List} of {@link String} session names
	 * @throws SessionDistributorServiceException
	 */
	List<String> getDistributedSessionNames() throws SessionDistributorServiceException;

	/**
	 * Update distributed session.
	 * 
	 * @param name
	 *            - session name
	 * @param attributes
	 *            - attributes
	 * @throws SessionDistributorServiceException
	 */
	void updateDistributedSession(String name, List<SessionAttribute> attributes) throws SessionDistributorServiceException;
}
