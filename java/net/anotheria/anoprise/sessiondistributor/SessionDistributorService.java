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

	String createDistributedSession(List<SessionAttribute> attributes) throws SessionDistributorServiceException;

	void deleteDistributedSession(String name) throws SessionDistributorServiceException;

	List<SessionAttribute> getDistributedSession(String name) throws SessionDistributorServiceException;

	List<SessionAttribute> getAndDeleteDistributedSession(String name) throws SessionDistributorServiceException;

	List<String> getDistributedSessionNames() throws SessionDistributorServiceException;

	void updateDistributedSession(String name, List<SessionAttribute> attributes) throws SessionDistributorServiceException;
}
