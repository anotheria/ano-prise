package net.anotheria.anoprise.sessiondistributor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.log4j.Logger;

import net.anotheria.util.IdCodeGenerator;

/**
 * SessionDestributorService implementation.
 * 
 * @author lrosenberg
 * @version 1.0, 2010/01/03
 */
public class SessionDistributorServiceImpl implements SessionDistributorService {

	private static Logger log = Logger.getLogger(SessionDistributorServiceImpl.class);
	
	/**
	 * Internal storage for session holders.
	 */
	private ConcurrentMap<String, SessionHolder> sessions;

	/**
	 * Default constructor.
	 */
	public SessionDistributorServiceImpl() {
		sessions = new ConcurrentHashMap<String, SessionHolder>();
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				try{
					cleanup();
				}catch(Exception e){
					log.error("Uncaught exception in cleanup() timer task",e);
				}
			}
		}, 0, 1000L*60*5);
	}

	@Override
	public String createDistributedSession(List<SessionAttribute> attributes) throws SessionDistributorServiceException {
		String name = null;
		SessionHolder old;
		do {
			name = IdCodeGenerator.generateCode(30);
			old = sessions.putIfAbsent(name, new SessionHolder(name, attributes));
		} while (old != null);
		return name;
	}

	@Override
	public void deleteDistributedSession(String name) throws SessionDistributorServiceException {

		if (!sessions.containsKey(name))
			throw new NoSuchDistributedSessionException(name);
		sessions.remove(name);
	}

	@Override
	public List<SessionAttribute> getAndDeleteDistributedSession(String name) throws SessionDistributorServiceException {

		SessionHolder holder = sessions.remove(name);
		if (holder == null)
			throw new NoSuchDistributedSessionException(name);
		return holder.getAttributes();
	}

	@Override
	public List<SessionAttribute> getDistributedSession(String name) throws SessionDistributorServiceException {
		SessionHolder holder = sessions.get(name);
		if (holder == null)
			throw new NoSuchDistributedSessionException(name);
		return holder.getAttributes();
	}

	@Override
	public List<String> getDistributedSessionNames() throws SessionDistributorServiceException {
		ArrayList<String> ret = new ArrayList<String>(sessions.size());
		ret.addAll(sessions.keySet());
		return ret;
	}

	@Override
	public void updateDistributedSession(String name, List<SessionAttribute> attributes) throws SessionDistributorServiceException {
		SessionHolder holder = sessions.get(name);
		if (holder == null)
			throw new NoSuchDistributedSessionException(name);
		holder.setAttributes(attributes);
	}
	
	/**
	 * 
	 */
	private void cleanup(){
		int expiredCount = 0;
		int sizeBefore = sessions.size();
		Collection<SessionHolder> holders = new ArrayList<SessionHolder>();
		holders.addAll(sessions.values());
		for (SessionHolder h : holders){
			if (h.isExpired()){
				expiredCount++;
				sessions.remove(h.getName());
			}
		}
		log.info("Finished session distributor cleanup run, removed sessions: " + expiredCount + ", sizeBefore: " + sizeBefore + ", sizeAfter: "
				+ sessions.size());
	}

}
