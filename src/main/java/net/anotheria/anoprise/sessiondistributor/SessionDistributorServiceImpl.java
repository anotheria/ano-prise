package net.anotheria.anoprise.sessiondistributor;

import net.anotheria.anoprise.eventservice.Event;
import net.anotheria.anoprise.eventservice.EventChannel;
import net.anotheria.anoprise.eventservice.EventServiceFactory;
import net.anotheria.anoprise.eventservice.ProxyType;
import net.anotheria.anoprise.eventservice.util.QueueFullException;
import net.anotheria.anoprise.eventservice.util.QueuedEventSender;
import net.anotheria.anoprise.sessiondistributor.cache.SDCache;
import net.anotheria.anoprise.sessiondistributor.cache.SDCacheUtil;
import net.anotheria.anoprise.sessiondistributor.events.SessionDistributorESConstants;
import net.anotheria.anoprise.sessiondistributor.events.SessionDistributorEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * SessionDistributorService implementation.
 *
 * @author lrosenberg, h3llka
 * @version 1.2, 2012/02/07
 */
public class SessionDistributorServiceImpl implements SessionDistributorService {

	/**
	 * Log4j logger.
	 */
	private static Logger LOG = LoggerFactory.getLogger(SessionDistributorServiceImpl.class);
	/**
	 * Length constant.
	 */
	public static final int SESSION_ID_LENGTH = 30;
	/**
	 * Internal storage for session holders.
	 */
	private SDCache sessions;

	/**
	 * SessionDistributorServiceConfig serviceConfig instance.
	 */
	private final SessionDistributorServiceConfig serviceConfig;

	/**
	 * Queued service event sender.
	 */
	private QueuedEventSender eventSender;
	/**
	 * Simple lock monitor.
	 */
	private final Object lock = new Object();

	/**
	 * /**
	 * Default constructor.
	 */
	public SessionDistributorServiceImpl() {
		serviceConfig = SessionDistributorServiceConfig.getInstance();

		//initialising CACHE! can be read from FS - or simply created!
		sessions = SDCacheUtil.createCache();

		initIntegration();

	}


	/**
	 * Initialise integration stuff.
	 */
	private void initIntegration() {
		//obtaining eventService channel
		EventChannel sessionDistributorEventChannel = EventServiceFactory.createEventService().obtainEventChannel(SessionDistributorESConstants.CHANNEL_NAME,
				ProxyType.PUSH_SUPPLIER_PROXY);
		eventSender = new QueuedEventSender("eventannounce", sessionDistributorEventChannel, serviceConfig.getSessionDistributorEventQueueSize(),
				serviceConfig.getSessionDistributorEventQueueSleepTime(), LOG);
		eventSender.start();

		//Adding shutdown HOOK if  required!!!
		if (serviceConfig.isWrightSessionsToFsOnShutdownEnabled())
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					//checking - for reconfiguration before execution!!
					if (serviceConfig.isWrightSessionsToFsOnShutdownEnabled())
						//persist to FS on Shutdown!
						SDCacheUtil.save(sessions);
				}
			});

		//init cleanUp timer!!
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
		LOG.debug("createDistributedSession({})", sessionId);

		if (!serviceConfig.isSessionsLimitEnabled())
			return sessions.createSession(sessionId);


		synchronized (lock) {
			if (sessions.getCount() >= serviceConfig.getMaxSessionsCount())
				throw new SessionsCountLimitReachedSessionDistributorServiceException(serviceConfig.getMaxSessionsCount());

			return sessions.createSession(sessionId);
		}
	}

	@Override
	public void deleteDistributedSession(String sessionId) throws SessionDistributorServiceException {
		LOG.debug("deleteDistributedSession({})", sessionId);
		sessions.removeSession(sessionId);
		Event delete = new Event(SessionDistributorESConstants.ORIGINATOR, SessionDistributorEvent.delete(sessionId));
		try {
			eventSender.push(delete);
		} catch (QueueFullException e) {
			LOG.error("Can't push Session delete event. Queue is Full. Event: " + delete, e);
		}

	}


	@Override
	public DistributedSessionVO restoreDistributedSession(String sessionId, String callerId) throws SessionDistributorServiceException {
		LOG.debug("restoreDistributedSession({}, {})", sessionId, callerId);
		DistributedSessionVO session = sessions.getSession(sessionId);

		Event restore = new Event(SessionDistributorESConstants.ORIGINATOR, SessionDistributorEvent.restore(session.getName(), callerId));
		LOG.debug("pushing event: {}", restore);
		try {
			eventSender.push(restore);
		} catch (QueueFullException e) {
			LOG.error("Can't push Session restore event. Queue is Full. Event: " + restore, e);
		}

		LOG.debug("pushing finished");

		return session;
	}

	@Override
	public List<String> getDistributedSessionNames() {
		return sessions.getSessionIds();
	}

	public void updateSessionUserId(String sessionId, String userId) throws SessionDistributorServiceException {
		LOG.debug("updateSessionUserId({}, {})", sessionId, userId);
		sessions.updateSessionUserId(sessionId, userId);

	}

	public void updateSessionEditorId(String sessionId, String editorId) throws SessionDistributorServiceException {
		LOG.debug("updateSessionEditorId({}, {})", sessionId, editorId);
		sessions.updateSessionEditorId(sessionId, editorId);
	}


	public void addDistributedAttribute(String sessionId, DistributedSessionAttribute attribute) throws SessionDistributorServiceException {
		LOG.debug("addDistributedAttribute({}, {})", sessionId, attribute);
		sessions.addAttribute(sessionId, attribute);

	}

	public void removeDistributedAttribute(String sessionId, String attributeName) throws SessionDistributorServiceException {
		LOG.debug("removeDistributedAttribute({}, {})", sessionId, attributeName);
		sessions.removeAttribute(sessionId, attributeName);
	}

	@Override
	public void keepDistributedSessionAlive(String sessionId) throws SessionDistributorServiceException {
		LOG.debug("keepDistributedSessionAlive({})", sessionId);
		sessions.updateCallTime(sessionId);

	}


	/**
	 * Clean up expired session from local cache  based on timer task.
	 * All cleaned session ids - will be send inside CleanUp event.
	 */
	private void cleanup() {
		int expiredCount = 0;
		int sizeBefore = sessions.getCount();
		List<String> cleanedSessionsIds = new ArrayList<>();
		Collection<DistributedSessionVO> holders = sessions.getSessions();

		for (DistributedSessionVO session : holders) {
			if (session.isExpired()) {
				try {
					sessions.removeSession(session.getName());
					expiredCount++;
					cleanedSessionsIds.add(session.getName());
				} catch (NoSuchDistributedSessionException e) {
					LOG.warn("cleanup() - detected already removed session '"+session.getName()+"'! clustering enabled: " + serviceConfig.isMultipleInstancesEnabled(), e);
				}

			}
		}
		//event !
		if (!cleanedSessionsIds.isEmpty()) {
			Event cleanUp = new Event(SessionDistributorESConstants.ORIGINATOR, SessionDistributorEvent.cleanUp(cleanedSessionsIds));
			try {
				LOG.debug("cleanup event sending: {}", cleanUp);
				eventSender.push(cleanUp);
			} catch (QueueFullException e) {
				LOG.error("Can't push session clean up event. Queue is full. Event:" + cleanUp, e);
			}
		}

		LOG.info("Finished session distributor cleanup run, removed sessions: {}, sizeBefore: {}, sizeAfter: {}", expiredCount, sizeBefore, sessions.getCount());
	}

}
