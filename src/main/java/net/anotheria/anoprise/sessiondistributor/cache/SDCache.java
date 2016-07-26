package net.anotheria.anoprise.sessiondistributor.cache;


import net.anotheria.anoprise.eventservice.Event;
import net.anotheria.anoprise.eventservice.EventServiceFactory;
import net.anotheria.anoprise.eventservice.EventServicePushConsumer;
import net.anotheria.anoprise.eventservice.util.QueuedEventReceiver;
import net.anotheria.anoprise.fs.FSSaveable;
import net.anotheria.anoprise.sessiondistributor.DistributedSessionAttribute;
import net.anotheria.anoprise.sessiondistributor.DistributedSessionVO;
import net.anotheria.anoprise.sessiondistributor.NoSuchDistributedSessionException;
import net.anotheria.anoprise.sessiondistributor.SessionDistributorServiceConfig;
import net.anotheria.anoprise.sessiondistributor.SessionDistributorServiceImpl;
import net.anotheria.anoprise.sessiondistributor.cache.events.SDCacheEvent;
import net.anotheria.anoprise.sessiondistributor.cache.events.SDCacheEventAnnouncer;
import net.anotheria.util.IdCodeGenerator;
import net.anotheria.util.StringUtils;
import net.anotheria.util.concurrency.IdBasedLock;
import net.anotheria.util.concurrency.IdBasedLockManager;
import net.anotheria.util.concurrency.SafeIdBasedLockManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Defines SDCache, with serialization possibility, etc.
 */
public final class SDCache implements FSSaveable {
	/**
	 * Basic serial version UID.
	 */
	private static final long serialVersionUID = -1947015503980653358L;
	/**
	 * Default id of current cache node in the cluster.
	 * Actually this Id will be used for NON clustered environment, as SERIALIZED cache file name.
	 */
	static final String DEFAULT_NODE_ID = "1000";
	/**
	 * Logger.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(SDCache.class);
	/**
	 * {@link IdBasedLockManager} instance.
	 */
	private transient IdBasedLockManager lockManager;

	/**
	 * {@link SessionDistributorServiceConfig} configuration.
	 */
	private transient SessionDistributorServiceConfig config;
	/**
	 * Event sender.
	 */
	private transient SDCacheEventAnnouncer announcer;
	/**
	 * Unique id of current cache in the cluster.
	 * Also serves as FileName for Serialized cache - on FS!
	 * If nodeId won't be provided via System.property with configured name - then DEFAULT_NODE_ID will be used.
	 * And cluster features won't be enabled.
	 */
	private transient String nodeId = DEFAULT_NODE_ID;

	/**
	 * Internal storage for session holders.
	 */
	private ConcurrentMap<String, DistributedSessionVO> sessions;

	/**
	 * Constructor.
	 */
	protected SDCache() {
		sessions = new ConcurrentHashMap<>();
		init();
	}

	/**
	 * Initialise, integration and internal stuff.
	 */
	private void init() {
		lockManager = new SafeIdBasedLockManager();
		config = SessionDistributorServiceConfig.getInstance();
		//checking that  clustering is enabled
		if (config.isMultipleInstancesEnabled())
			configureClusterIntegration();
	}

	/**
	 * Configure integration STUFF.
	 */
	private void configureClusterIntegration() {
		LOG.info("Cluster integration configuration started");
		String systemPropertyNodeId = System.getProperty(config.getNodeIdSystemPropertyName());
		if (StringUtils.isEmpty(systemPropertyNodeId)) {
			LOG.warn("SD -  Cluster configuration FAILED. Required nodeId property[{}] is absent in SystemProperties, or badly configured! Working in standAlone mode!", config.getNodeIdSystemPropertyName());
			return;
		}
		nodeId = systemPropertyNodeId;

		//announcer!
		announcer = new SDCacheEventAnnouncer();

		//receiver configuration
		SDCacheEventsConsumer cacheConsumer = new SDCacheEventsConsumer();
		QueuedEventReceiver sdCacheEventReceiver = new QueuedEventReceiver("SDCacheEventReceiver",
				SDCacheEventAnnouncer.EVENT_CHANNEL_NAME, cacheConsumer,
				config.getSdCacheEventQueueSize(),
				config.getSessionDistributorEventQueueSleepTime(), LOG);
		//consumer registration
		EventServiceFactory.createEventService().obtainEventChannel(SDCacheEventAnnouncer.EVENT_CHANNEL_NAME, sdCacheEventReceiver).addConsumer(cacheConsumer);
		sdCacheEventReceiver.start();

		LOG.info("Cluster integration stuff successfully configured! NodeId{{}}", nodeId);
	}

	/**
	 * Returns cached sessions count.
	 *
	 * @return amount of cached sessions
	 */
	public int getCount() {
		return sessions.size();
	}

	/**
	 * Returns all cached sessions.
	 *
	 * @return {@link java.util.List <DistributedSessionVO>}
	 */
	public List<DistributedSessionVO> getSessions() {
		return new ArrayList<>(sessions.values());
	}

	/**
	 * Return session ids.
	 *
	 * @return ids collection
	 */
	public List<String> getSessionIds() {
		return new ArrayList<>(sessions.keySet());
	}

	/**
	 * Return true if session with selected id exists.
	 *
	 * @param sessionId id of distributed session
	 * @return boolean value
	 */
	public boolean sessionExists(String sessionId) {
		return sessions.containsKey(sessionId);
	}

	/**
	 * Create session.
	 *
	 * @param possibleSessionId possible id of session.
	 * @return session id
	 */
	public String createSession(String possibleSessionId) {
		DistributedSessionVO toCreate = new DistributedSessionVO(possibleSessionId);
		if (!sessionExists(possibleSessionId)) {
			sessions.put(possibleSessionId, toCreate);
			//send event!  CREATE
			announceSave(toCreate);
			return possibleSessionId;
		}
		DistributedSessionVO old;
		do {
			toCreate.setName(IdCodeGenerator.generateCode(SessionDistributorServiceImpl.SESSION_ID_LENGTH));
			old = sessions.putIfAbsent(toCreate.getName(), toCreate);
		} while (old != null);

		//send event!  CREATE
		announceSave(toCreate);

		return toCreate.getName();
	}


	/**
	 * Remove session with selected id.
	 *
	 * @param sessionId id of session
	 * @throws net.anotheria.anoprise.sessiondistributor.NoSuchDistributedSessionException
	 *          if session does not exists
	 */
	public void removeSession(String sessionId) throws NoSuchDistributedSessionException {
		IdBasedLock lock = lockManager.obtainLock(sessionId);
		lock.lock();
		try {
			DistributedSessionVO session = getSession(sessionId);
			sessions.remove(sessionId);
			//Announce delete
			announceDelete(session);
		} finally {
			lock.unlock();
		}

	}


	/**
	 * Returns distributed session if such exists.
	 *
	 * @param sessionId session id
	 * @return {@link DistributedSessionVO} if such exists
	 * @throws NoSuchDistributedSessionException
	 *          if session does not exists
	 */
	public DistributedSessionVO getSession(String sessionId) throws NoSuchDistributedSessionException {
		if (!sessionExists(sessionId) || sessions.get(sessionId) == null)
			throw new NoSuchDistributedSessionException(sessionId);
		return sessions.get(sessionId);
	}

	/**
	 * Update userId property for session with selected id.
	 *
	 * @param sessionId id of the session
	 * @param userId	user id
	 * @throws NoSuchDistributedSessionException
	 *          if no  such session exists
	 */
	public void updateSessionUserId(String sessionId, String userId) throws NoSuchDistributedSessionException {
		IdBasedLock lock = lockManager.obtainLock(sessionId);
		lock.lock();
		try {
			DistributedSessionVO session = getSession(sessionId);
			session.setUserId(userId);
			session.setLastChangeTime(System.currentTimeMillis());
			//update event
			announceSave(session);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Update editorId property for session with selected id.
	 *
	 * @param sessionId id of the session
	 * @param editorId  user id
	 * @throws NoSuchDistributedSessionException
	 *          if no  such session exists
	 */
	public void updateSessionEditorId(String sessionId, String editorId) throws NoSuchDistributedSessionException {
		IdBasedLock lock = lockManager.obtainLock(sessionId);
		lock.lock();
		try {
			DistributedSessionVO session = getSession(sessionId);
			session.setEditorId(editorId);
			session.setLastChangeTime(System.currentTimeMillis());
			//update event
			announceSave(session);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Add attribute to session with selected id.
	 *
	 * @param sessionId id of the session
	 * @param attribute {@link net.anotheria.anoprise.sessiondistributor.DistributedSessionAttribute}
	 * @throws NoSuchDistributedSessionException
	 *          if no  such session exists
	 */
	public void addAttribute(String sessionId, DistributedSessionAttribute attribute) throws NoSuchDistributedSessionException {
		IdBasedLock lock = lockManager.obtainLock(sessionId);
		lock.lock();
		try {
			DistributedSessionVO session = getSession(sessionId);
			session.addDistributedAttribute(attribute);
			session.setLastChangeTime(System.currentTimeMillis());
			//update event
			announceSave(session);
		} finally {
			lock.unlock();
		}

	}


	/**
	 * Remove attribute to session with selected id.
	 *
	 * @param sessionId id of the session
	 * @param attribute session attribute name
	 * @throws NoSuchDistributedSessionException
	 *          if no  such session exists
	 */
	public void removeAttribute(String sessionId, String attribute) throws NoSuchDistributedSessionException {
		IdBasedLock lock = lockManager.obtainLock(sessionId);
		lock.lock();
		try {
			DistributedSessionVO session = getSession(sessionId);
			session.removeDistributedAttribute(attribute);
			session.setLastChangeTime(System.currentTimeMillis());
			//update event ! should send Update announce even if no Remove of attribute was  performed! cause at  least UseTime  was updated!!!:)
			announceSave(session);
		} finally {
			lock.unlock();
		}

	}

	/**
	 * Updates session call time
	 *
	 * @param sessionId id of the session
	 * @throws NoSuchDistributedSessionException
	 *          if no  such session exists
	 */
	public void updateCallTime(String sessionId) throws NoSuchDistributedSessionException {
		IdBasedLock lock = lockManager.obtainLock(sessionId);
		lock.lock();
		try {
			DistributedSessionVO session = getSession(sessionId);
			session.setLastChangeTime(System.currentTimeMillis());
			//update event
			announceSave(session);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public String getOwnerId() {
		return nodeId;
	}

	@Override
	public String toString() {
        return "SDCache{" +
				"sessions-size=" + sessions.size() +
				", ownerId='" + nodeId + '\'' +
				'}';
	}

	/**
	 * Serialization method.
	 *
	 * @param oos - {@link java.io.ObjectOutputStream}
	 * @throws java.io.IOException on serialization errors
	 */
	private void writeObject(ObjectOutputStream oos) throws IOException {
		ObjectOutputStream.PutField fields = oos.putFields();
		fields.put("sessions", sessions);
		oos.writeFields();
	}

	/**
	 * DeSerialization method.
	 *
	 * @param ois - {@link java.io.ObjectInputStream}
	 * @throws ClassNotFoundException on DeSerialization errors
	 * @throws java.io.IOException	on DeSerialization errors
	 */

	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
		ObjectInputStream.GetField fields = ois.readFields();
		sessions = (ConcurrentMap<String, DistributedSessionVO>) fields.get("sessions", new ConcurrentHashMap<String, DistributedSessionVO>());
		LOG.info("Reading persisted sessions started");
		//prolong life time! after reading from file!
		long time = System.currentTimeMillis();
		for (DistributedSessionVO session : sessions.values())
			session.setLastChangeTime(time);

		//execute initialization!
		init();
		LOG.info("Reading persisted Sessions completed. {} sessions read successfully. Last change time was updated.", sessions.size());
	}


	/**
	 * Announce save event to cluster.
	 *
	 * @param session {@link DistributedSessionVO}
	 */
	private void announceSave(DistributedSessionVO session) {
		if (!config.isMultipleInstancesEnabled())
			return;
		if (announcer == null)
			return;
		announcer.sessionSave(nodeId, session);

	}

	/**
	 * Announce delete to cluster.
	 *
	 * @param session {@link DistributedSessionVO}
	 */
	private void announceDelete(DistributedSessionVO session) {
		if (!config.isMultipleInstancesEnabled())
			return;
		if (announcer == null)
			return;
		announcer.sessionDelete(nodeId, session);
	}


	/**
	 * Remove session. Method is used from Consumer!
	 *
	 * @param incomingNodeId node id
	 * @param session		{@link DistributedSessionVO to remove}
	 */
	private void remove(String incomingNodeId, DistributedSessionVO session) {
		boolean isDebug = LOG.isDebugEnabled();
		if (isDebug)
			LOG.debug("Called remove({},{})", incomingNodeId, session);
		//should not handle! This is the event from current SDCache!
		if (nodeId.equals(incomingNodeId))
			return;

		IdBasedLock lock = lockManager.obtainLock(session.getName());
		lock.lock();
		try {
			try {
				DistributedSessionVO onCurrentNode = getSession(session.getName());

				// remove   if it's possible!!!
				if (onCurrentNode.getLastChangeTime() <= session.getLastChangeTime()) {
					sessions.remove(session.getName());
					if (isDebug)
						LOG.debug("Session '{}' removed by delete event, on node '{}'. Event comes from remote node[{}] ", session.getName(), nodeId, incomingNodeId);
				}

				if (isDebug)
					LOG.debug("Session '{}' can't be removed by delete event on node '{}', because it is still in use. Event sent by remote node '{}'", session.getName(), nodeId, incomingNodeId);


			} catch (NoSuchDistributedSessionException e) {
				LOG.warn("Session '"+session.getName()+"' does not exist on node '"+nodeId+"'. Exception occurred on delete try of session. Deletion event sent by remote node '"+incomingNodeId+ '\'', e);
			}

		} finally {
			lock.unlock();
		}

	}

	/**
	 * Save session. Method is used from Consumer!
	 *
	 * @param incomingNodeId node id
	 * @param session		{@link DistributedSessionVO to remove}
	 */
	private void save(String incomingNodeId, DistributedSessionVO session) {
		boolean isDebug = LOG.isDebugEnabled();
		if (isDebug)
			LOG.debug("Called save({},{})", incomingNodeId, session);
		//should not handle! This is the event from current SDCache!
		if (nodeId.equals(incomingNodeId))
			return;


		IdBasedLock lock = lockManager.obtainLock(session.getName());
		lock.lock();
		try {
			try {
				DistributedSessionVO onCurrentNode = getSession(session.getName());

				// update   if it's possible!!!
				if (onCurrentNode.getLastChangeTime() < session.getLastChangeTime()) {
					sessions.put(session.getName(), session);
					if (isDebug)
						LOG.debug("Session[{}] updated on node[{}] by event from remote_node[{}]", session.getName(), nodeId, incomingNodeId);
				}

			} catch (NoSuchDistributedSessionException e) {
				sessions.put(session.getName(), session);
				LOG.error("Session '"+session.getName()+"' created on node '"+nodeId+"', by event sent from remote_node '"+incomingNodeId+ '\'', e);
			}

		} finally {
			lock.unlock();
		}

	}


	/**
	 * Events consumer - for SDCache.
	 */
	private class SDCacheEventsConsumer implements EventServicePushConsumer {
		/**
		 * {@link Logger} instance.
		 */
		private final Logger log = LoggerFactory.getLogger(SDCacheEventsConsumer.class);

		@Override
		public void push(Event incomingEvent) {
			if (log.isDebugEnabled())
				log.debug("SDCacheEvent: {}", incomingEvent);
			if (incomingEvent == null || incomingEvent.getData() == null || !(incomingEvent.getData() instanceof SDCacheEvent))
				return;

			SDCacheEvent cacheEvent = SDCacheEvent.class.cast(incomingEvent.getData());
			switch (cacheEvent.getOperation()) {
				case CACHE_SESSION_SAVE:
					save(cacheEvent.getClusterNodeId(), cacheEvent.getSession());
					break;

				case CACHE_SESSION_REMOVE:
					remove(cacheEvent.getClusterNodeId(), cacheEvent.getSession());
					break;


				default:
					log.warn("{} NOT supported in current implementation", cacheEvent.getOperation().name());
			}

		}
	}


}