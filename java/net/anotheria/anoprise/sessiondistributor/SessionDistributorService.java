package net.anotheria.anoprise.sessiondistributor;

import java.util.List;

import net.anotheria.anoprise.metafactory.Service;

public interface SessionDistributorService extends Service{
	public String createDistributedSession(List<SessionAttribute> attributes) throws SessionDistributorServiceException;
	
	public void deleteDistributedSession(String name) throws SessionDistributorServiceException;
	
	public List<SessionAttribute> getDistributedSession(String name) throws SessionDistributorServiceException;
	
	public List<SessionAttribute> getAndDeleteDistributedSession(String name) throws SessionDistributorServiceException;
	
	public List<String> getDistributedSessionNames() throws SessionDistributorServiceException;
	
	public void updateDistributedSession(String name, List<SessionAttribute> attributes) throws SessionDistributorServiceException;
}
