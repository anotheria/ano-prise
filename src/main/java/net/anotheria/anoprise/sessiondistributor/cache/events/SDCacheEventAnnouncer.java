package net.anotheria.anoprise.sessiondistributor.cache.events;

import net.anotheria.anoprise.eventservice.Event;
import net.anotheria.anoprise.eventservice.EventChannel;
import net.anotheria.anoprise.eventservice.EventServiceFactory;
import net.anotheria.anoprise.eventservice.EventServicePushSupplier;
import net.anotheria.anoprise.eventservice.util.QueueFullException;
import net.anotheria.anoprise.eventservice.util.QueuedEventSender;
import net.anotheria.anoprise.sessiondistributor.DistributedSessionVO;
import net.anotheria.anoprise.sessiondistributor.SessionDistributorServiceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Event announcer for SD cache.
 *
 * @author h3ll
 */
public final class SDCacheEventAnnouncer implements EventServicePushSupplier {
	/**
	 * Channel name.
	 */
	public static final String EVENT_CHANNEL_NAME = "SessionDistributorServiceCache";
	/**
	 * Originator.
	 */
	public static final String ORIGINATOR = "-SessionDistributorCache-";
	/**
	 * Property name which will avoid ES - start in local mode... for  tests etc.
	 */
	private static final String JUNITTEST = "JUNITTEST";
	/**
	 * Sender for the events.
	 */
	private QueuedEventSender eventSender;
	/**
	 * Logger.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(SDCacheEventAnnouncer.class);

	/**
	 * Constructor.
	 */
	public SDCacheEventAnnouncer() {
		SessionDistributorServiceConfig config = SessionDistributorServiceConfig.getInstance();
		EventChannel eventChannel = EventServiceFactory.createEventService().obtainEventChannel(EVENT_CHANNEL_NAME, this);
		boolean unitTesting = Boolean.valueOf(System.getProperty(JUNITTEST, String.valueOf(false)));
		eventSender = new QueuedEventSender(EVENT_CHANNEL_NAME + "-sender", eventChannel, config.getSdCacheEventQueueSize(), config.getSdCacheEventQueueSleepTime(), LOG);
		if (unitTesting) {
			eventSender.setSynchedMode(true);
		} else {
			eventSender.start();
		}
	}

	/**
	 * Save session.
	 *
	 * @param nodeId  id of node
	 * @param session {@link DistributedSessionVO}
	 */
	public void sessionSave(String nodeId, DistributedSessionVO session) {
		deliver(SDCacheEvent.save(nodeId, session));
	}

	/**
	 * Delete session.
	 *
	 * @param nodeId  id of node
	 * @param session {@link DistributedSessionVO}
	 */
	public void sessionDelete(String nodeId, DistributedSessionVO session) {
		deliver(SDCacheEvent.delete(nodeId, session));
	}

	/**
	 * Internal method that delivers the events into the channel.
	 *
	 * @param eventData {@link SDCacheEvent}
	 */
	private void deliver(SDCacheEvent eventData) {
		Event event = new Event(eventData);
		event.setOriginator(ORIGINATOR);
		try {
			eventSender.push(event);
		} catch (QueueFullException e) {
			LOG.error("Couldn't publish event '"+event+"' due to queue overflow", e);
		}
	}
}
