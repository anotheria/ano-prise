package net.anotheria.anoprise.sessiondistributor;

import net.anotheria.anoprise.eventservice.Event;
import net.anotheria.anoprise.eventservice.EventChannel;
import net.anotheria.anoprise.eventservice.EventServiceFactory;
import net.anotheria.anoprise.eventservice.ProxyType;
import net.anotheria.anoprise.eventservice.util.QueueFullException;
import net.anotheria.anoprise.eventservice.util.QueuedEventSender;
import net.anotheria.anoprise.sessiondistributor.events.SessionDistributorESConstants;
import net.anotheria.anoprise.sessiondistributor.events.SessionDistributorEvent;
import net.anotheria.util.IdCodeGenerator;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * SessionDistributorService implementation.
 *
 * @author lrosenberg
 * @version 1.0, 2010/01/03
 */
public class SessionDistributorServiceImpl implements SessionDistributorService {

	/**
	 * Log4j logger.
	 */
	private static Logger LOG = Logger.getLogger(SessionDistributorServiceImpl.class);

	/**
	 * Internal storage for session holders.
	 */
	private ConcurrentMap<String, DistributedSessionVO> sessions;

	/**
	 * SessionDistributorServiceConfig serviceConfig instance.
	 */
	private SessionDistributorServiceConfig serviceConfig;

	/**
	 * Queued service event sender.
	 */
	private QueuedEventSender eventSender;

	/**
	 * Default constructor.
	 */
	public SessionDistributorServiceImpl() {
		sessions = new ConcurrentHashMap<String, DistributedSessionVO>();
		serviceConfig = SessionDistributorServiceConfig.getInstance();
		initCleanUpTimerTask();
		//obtaining eventService channel
		EventChannel sessionDistributorEventChannel = EventServiceFactory.createEventService().obtainEventChannel(SessionDistributorESConstants.CHANNEL_NAME,
				ProxyType.PUSH_SUPPLIER_PROXY);
		eventSender = new QueuedEventSender("eventannounce", sessionDistributorEventChannel, serviceConfig.getSessionDistributorEventQueueSize(),
				serviceConfig.getSessionDistributorEventQueueSleepTime(), LOG);
		eventSender.start();
	}

	/**
	 * CleanUp Timer task initialisation.
	 */
	private void initCleanUpTimerTask() {
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				try {
					cleanup();
				} catch (Exception e) {
					LOG.error("Uncaught exception in cleanup() timer task", e);
				}
			}
		}, 0, serviceConfig.getDistributedSessionsCleanUpInterval());
	}

	@Override
	public String createDistributedSession(String sessionId) throws SessionDistributorServiceException {
		LOG.debug("createDistributedSession("+sessionId+")");
		DistributedSessionVO toCreate = new DistributedSessionVO(sessionId);
		if (!sessions.containsKey(sessionId)) {
			sessions.put(sessionId, toCreate);
			return sessionId;
		}
		DistributedSessionVO old;
		do {
			toCreate.setName(IdCodeGenerator.generateCode(30));
			old = sessions.putIfAbsent(toCreate.getName(), toCreate);
		} while (old != null);

		return toCreate.getName();
	}

	@Override
	public void deleteDistributedSession(String name) throws SessionDistributorServiceException {
		LOG.debug("deleteDistributedSession("+name+")");
		if (!sessions.containsKey(name))
			throw new NoSuchDistributedSessionException(name);
		sessions.remove(name);
		Event delete = new Event(SessionDistributorESConstants.ORIGINATOR, SessionDistributorEvent.delete(name));
		try {
			eventSender.push(delete);
		} catch (QueueFullException e) {
			LOG.error("Can't push Session delete event. Queue is Full. Event:" + delete);
		}

	}


	@Override
	public DistributedSessionVO restoreDistributedSession(String name, String callerId) throws SessionDistributorServiceException {
		LOG.debug("restoreDistributedSession("+name+", "+callerId+")");
		DistributedSessionVO session = getDistributedSession(name);

		Event restore = new Event(SessionDistributorESConstants.ORIGINATOR, SessionDistributorEvent.restore(session.getName(), callerId));
		LOG.debug("pushing event: "+restore);
		try {
			eventSender.push(restore);
		} catch (QueueFullException e) {
			LOG.error("Can't push Session restore event. Queue is Full. Event:" + restore);
		}

		LOG.debug("pushing finished");

		return session;
	}

	@Override
	public List<String> getDistributedSessionNames() throws SessionDistributorServiceException {
		ArrayList<String> ret = new ArrayList<String>(sessions.size());
		ret.addAll(sessions.keySet());
		return ret;
	}

	public void updateSessionUserId(String sessionName, String userId) throws SessionDistributorServiceException {
		DistributedSessionVO session = getDistributedSession(sessionName);
		session.setUserId(userId);
		session.setLastChangeTime(System.currentTimeMillis());
	}

	public void updateSessionEditorId(String sessionName, String editorId) throws SessionDistributorServiceException {
		DistributedSessionVO session = getDistributedSession(sessionName);
		session.setEditorId(editorId);
		session.setLastChangeTime(System.currentTimeMillis());
	}


	public void addDistributedAttribute(String sessionName, DistributedSessionAttribute attribute) throws SessionDistributorServiceException {
		LOG.debug("addDistributedAttribute("+sessionName+", "+attribute+")");
		DistributedSessionVO session = getDistributedSession(sessionName);
		session.addDistributedAttribute(attribute);
		session.setLastChangeTime(System.currentTimeMillis());
	}

	public void removeDistributedAttribute(String sessionName, String attributeName) throws SessionDistributorServiceException {
		LOG.debug("removeDistributedAttribute("+sessionName+", "+attributeName+")");
		DistributedSessionVO session = getDistributedSession(sessionName);
		session.removeDistributedAttribute(attributeName);
		session.setLastChangeTime(System.currentTimeMillis());
	}

	@Override
	public void keepDistributedSessionAlive(String sessionName) throws SessionDistributorServiceException {
		LOG.debug("keepDistributedSessionAlive("+sessionName+")");
		DistributedSessionVO session = getDistributedSession(sessionName);
		session.setLastChangeTime(System.currentTimeMillis());
	}

	/**
	 * Return DistributedSessionVO from local cache.
	 * If such session does not exists NoSuchDistributedSessionException will be thrown.
	 *
	 * @param name - session name
	 * @return {@link DistributedSessionVO}
	 * @throws NoSuchDistributedSessionException
	 *          if session does not exist
	 */
	private DistributedSessionVO getDistributedSession(String name) throws NoSuchDistributedSessionException {
		DistributedSessionVO session = sessions.get(name);
		if (session == null)
			throw new NoSuchDistributedSessionException(name);
		return session;
	}

	/**
	 * Clean up expired session from local cache  based on timer task.
	 * All cleaned session ids - will be send inside CleanUp event.
	 */
	private void cleanup() {
		int expiredCount = 0;
		int sizeBefore = sessions.size();
		List<String> cleanedSessionsIds = new ArrayList<String>();
		Collection<DistributedSessionVO> holders = new ArrayList<DistributedSessionVO>();
		holders.addAll(sessions.values());
		for (DistributedSessionVO session : holders) {
			if (session.isExpired()) {
				expiredCount++;
				sessions.remove(session.getName());
				cleanedSessionsIds.add(session.getName());
			}
		}
		//event !
		if (!cleanedSessionsIds.isEmpty()) {
			Event cleanUp = new Event(SessionDistributorESConstants.ORIGINATOR, SessionDistributorEvent.cleanUp(cleanedSessionsIds));
			try {
				LOG.debug("cleanup event sending: "+cleanUp);
				eventSender.push(cleanUp);
			} catch (QueueFullException e) {
				LOG.error("Can't push Session cleanUp event. Queue is Full. Event:" + cleanUp);
			}
		}

		LOG.info("Finished session distributor cleanup run, removed sessions: " + expiredCount + ", sizeBefore: " + sizeBefore + ", sizeAfter: "
				+ sessions.size());
	}

}
