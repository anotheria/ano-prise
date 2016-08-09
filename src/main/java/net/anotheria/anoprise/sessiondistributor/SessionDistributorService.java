package net.anotheria.anoprise.sessiondistributor;

import net.anotheria.anoprise.metafactory.Service;

import java.util.List;

/**
 * SessionDistributorService interface.
 *
 * @author lrosenberg
 * @version 1.0, 2010/01/03
 */
public interface SessionDistributorService extends Service {

	/**
	 * Create new distributed session without attributes.
	 * SessionId parameter will be used as session ID - if  such  does not exist! Otherwise sessionId will be generated!
	 *
	 * @param sessionId - possible session Id
	 * @return created session id
	 * @throws SessionDistributorServiceException
	 *          on errors
	 *          {@link SessionsCountLimitReachedSessionDistributorServiceException} in case when max configured session amount - is reached.
	 */
	String createDistributedSession(String sessionId) throws SessionDistributorServiceException;

	/**
	 * Delete distributed session.
	 *
	 * @param sessionId - session sessionId
	 * @throws SessionDistributorServiceException
	 *          - {@link NoSuchDistributedSessionException} - if session does not exist.
	 */
	void deleteDistributedSession(String sessionId) throws SessionDistributorServiceException;


	/**
	 * Return distributed session if such exists. Afterwards session restored event will be generated.
	 *
	 * @param sessionId - session sessionId
	 * @param callerId  - actually caller service id (will be used only in event!)
	 * @return {@link DistributedSessionVO}
	 * @throws SessionDistributorServiceException
	 *          - {@link NoSuchDistributedSessionException} - if session does not exist.
	 */
	DistributedSessionVO restoreDistributedSession(String sessionId, String callerId) throws SessionDistributorServiceException;

	/**
	 * Get distributed session names.
	 *
	 * @return {@link List} of {@link String} session names
	 */
	List<String> getDistributedSessionNames();

	/**
	 * Updates DistributedSession userId property.
	 *
	 * @param sessionId name of the session
	 * @param userId    user id
	 * @throws SessionDistributorServiceException
	 *          - {@link NoSuchDistributedSessionException} - if session does not exist.
	 */
	void updateSessionUserId(String sessionId, String userId) throws SessionDistributorServiceException;

	/**
	 * Updates DistributedSession editorId property.
	 *
	 * @param sessionId name of the session
	 * @param editorId  editor id
	 * @throws SessionDistributorServiceException
	 *          - {@link NoSuchDistributedSessionException} - if session does not exist.
	 */
	void updateSessionEditorId(String sessionId, String editorId) throws SessionDistributorServiceException;

	/**
	 * Add attribute to session.
	 * If such attribute already exist - it will be updated.
	 *
	 * @param sessionId - name of the session
	 * @param attribute - {@link DistributedSessionAttribute}
	 * @throws SessionDistributorServiceException
	 *          - {@link NoSuchDistributedSessionException} - if session does not exist.
	 */
	void addDistributedAttribute(String sessionId, DistributedSessionAttribute attribute) throws SessionDistributorServiceException;

	/**
	 * Remove distributed attribute with selected attributeName from session.
	 *
	 * @param sessionId     -  name of the session
	 * @param attributeName - attribute name
	 * @throws SessionDistributorServiceException
	 *          - {@link NoSuchDistributedSessionException} - if session does not exist.
	 */
	void removeDistributedAttribute(String sessionId, String attributeName) throws SessionDistributorServiceException;

	/**
	 * Keep alive call. Actually simply prolong distributed session expiration time!
	 *
	 * @param sessionId name/id of the  session
	 * @throws SessionDistributorServiceException
	 *          on errors
	 *          - {@link NoSuchDistributedSessionException} - if session does not exist.
	 */
	void keepDistributedSessionAlive(String sessionId) throws SessionDistributorServiceException;
}
