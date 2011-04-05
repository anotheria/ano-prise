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
	 * @throws SessionDistributorServiceException
	 *          on errors
	 * @return created session id
	 */
	String createDistributedSession(String sessionId) throws SessionDistributorServiceException;

	/**
	 * Delete distributed session.
	 *
	 * @param name - session name
	 * @throws SessionDistributorServiceException
	 *          - {@link NoSuchDistributedSessionException} - if session does not exist.
	 */
	void deleteDistributedSession(String name) throws SessionDistributorServiceException;


	/**
	 * Return distributed session if such exists. Afterwards session restored event will be generated.
	 *
	 * @param name - session name
	 * @param callerId - actually caller service id (will be used only in event!)
	 * @return {@link DistributedSessionVO}
	 * @throws SessionDistributorServiceException
	 *          - {@link NoSuchDistributedSessionException} - if session does not exist.
	 */
	DistributedSessionVO restoreDistributedSession(String name, String callerId) throws SessionDistributorServiceException;

	/**
	 * Get distributed session names.
	 *
	 * @return {@link List} of {@link String} session names
	 * @throws SessionDistributorServiceException  on errors
	 *
	 */
	List<String> getDistributedSessionNames() throws SessionDistributorServiceException;

	/**
	 * Updates DistributedSession userId property.
	 *
	 * @param sessionName name of the session
	 * @param userId	  user id
	 * @throws SessionDistributorServiceException
	 *          - {@link NoSuchDistributedSessionException} - if session does not exist.
	 */
	void updateSessionUserId(String sessionName, String userId) throws SessionDistributorServiceException;

	/**
	 * Updates DistributedSession editorId property.
	 *
	 * @param sessionName name of the session
	 * @param editorId	editor id
	 * @throws SessionDistributorServiceException
	 *          - {@link NoSuchDistributedSessionException} - if session does not exist.
	 */
	void updateSessionEditorId(String sessionName, String editorId) throws SessionDistributorServiceException;

	/**
	 * Add attribute to session.
	 * If such attribute already exist - it will be updated.
	 *
	 * @param sessionName - name of the session
	 * @param attribute   - {@link DistributedSessionAttribute}
	 * @throws SessionDistributorServiceException
	 *          - {@link NoSuchDistributedSessionException} - if session does not exist.
	 */
	void addDistributedAttribute(String sessionName, DistributedSessionAttribute attribute) throws SessionDistributorServiceException;

	/**
	 * Remove distributed attribute woth selected attributeName from session.
	 *
	 * @param sessionName   -  name of the session
	 * @param attributeName - attribute name
	 * @throws SessionDistributorServiceException
	 *          - {@link NoSuchDistributedSessionException} - if session does not exist.
	 */
	void removeDistributedAttribute(String sessionName, String attributeName) throws SessionDistributorServiceException;


}
