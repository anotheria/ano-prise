package net.anotheria.anoprise.sessiondistributor;

import java.util.List;

public interface SessionDistributorService {
	public String createDistributedSession(List<SessionAttribute> attributes) throws SessionDistributorServiceException;
	
	public void deleteDistributedSession(String name) throws SessionDistributorServiceException;
	
	public List<SessionAttribute> getDistributedSession(String name) throws SessionDistributorServiceException;
	
	public List<SessionAttribute> getAndDeleteDistributedSession(String name) throws SessionDistributorServiceException;
	
	public List<String> getDistributedSessionNames() throws SessionDistributorServiceException;
}
