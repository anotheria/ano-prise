package net.anotheria.anoprise.eventservice.util;

import net.anotheria.anoprise.eventservice.Event;
import net.anotheria.anoprise.eventservice.EventServicePushConsumer;
import net.anotheria.util.queue.IQueue;
import net.anotheria.util.queue.QueueOverflowException;
import net.anotheria.util.queue.StandardQueueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueuedEventReceiver extends Thread implements EventServicePushConsumer {

	/**
	 * Default logger.
	 */
	private static final Logger DEFAULT_LOGGER = LoggerFactory.getLogger(QueuedEventReceiver.class.getName());

	/**
	 * Logger.
	 */
	private Logger logger;

	/**
	 * Default queue size.
	 */
	public static final int DEF_QUEUE_SIZE = 1000;

	/**
	 * Queue size.
	 */
	private int queueSize;

	/**
	 * Default sleep time.
	 */
	public static final long DEF_SLEEP_TIME = 50;

	/**
	 * Queue sleep time.
	 */
	private long sleepTime;

	/**
	 * Receiver name.
	 */
	private String name;

	/**
	 * Event channel name.
	 */
	private String channelName;

	/**
	 * Received events count.
	 */
	private int receivedCount;

	/**
	 * Queue overflows count.
	 */
	private int overflowCount;
	/**
	 * Thrown away events count.
	 */
	private int throwAwayCount;

	/**
	 * Asynchronous queue for receive events.
	 */
	private IQueue<Event> queue;

	/**
	 * Event consumer.
	 */
	private EventServicePushConsumer eventConsumer;

	/**
	 * Default constructor.
	 * 
	 * @param aName
	 *            - receiver name
	 * @param aChannelName
	 *            - event channel name
	 */
	public QueuedEventReceiver(String aName, String aChannelName, EventServicePushConsumer aEventConsumer) {
		this(aName, aChannelName, aEventConsumer, DEF_QUEUE_SIZE, DEF_SLEEP_TIME);
	}

	/**
	 * Default constructor.
	 * 
	 * @param aName
	 *            - receiver name
	 * @param aChannelName
	 *            - event channel name
	 * @param aQueueSize
	 *            - queue size
	 * @param aSleepTime
	 *            - queue sleep time
	 */
	public QueuedEventReceiver(String aName, String aChannelName, EventServicePushConsumer aEventConsumer, int aQueueSize, long aSleepTime) {
		this(aName, aChannelName, aEventConsumer, aQueueSize, aSleepTime, DEFAULT_LOGGER);
	}

	/**
	 * Default constructor.
	 * 
	 * @param aName
	 *            - receiver name
	 * @param aChannelName
	 *            - event channel name
	 * @param aQueueSize
	 *            - queue size
	 * @param aSleepTime
	 *            - queue sleep time
	 * @param aLogger
	 *            - logger
	 */
	public QueuedEventReceiver(String aName, String aChannelName, EventServicePushConsumer aEventConsumer, int aQueueSize, long aSleepTime, Logger aLogger) {
		super(aName);
		setDaemon(true);
		if (aName == null)
			throw new IllegalArgumentException("Receiver name must be not null");

		if (aChannelName == null)
			throw new IllegalArgumentException("Event channel name must be not null");

		if (aEventConsumer == null)
			throw new IllegalArgumentException("Event consumer name must be not null");

		if (aLogger == null)
			throw new IllegalArgumentException("Logger must be not null");

		this.name = aName;
		this.channelName = aChannelName;
		this.eventConsumer = aEventConsumer;
		this.queueSize = aQueueSize;
		this.sleepTime = aSleepTime;
		this.logger = aLogger;
		this.queue = new StandardQueueFactory<Event>().createQueue(queueSize);
	}

	@Override
	public void push(Event event) {
		try {
			queue.putElement(event);
		} catch (QueueOverflowException e1) {
			overflowCount++;

			try {
				Thread.sleep(sleepTime);
			} catch (Exception ignored) {
			}

			try {
				queue.putElement(event);
			} catch (QueueOverflowException e2) {
				throwAwayCount++;
				logger.error("Couldn't recover from queue overflow, throwing away " + event);
			}
		}
	}

	@Override
	public void run() {
		try {
			receivedCount = 0;
			while (true) {
				if (queue.hasElements()) {
					receivedCount++;
					if ((receivedCount / 100 * 100) == receivedCount)
						logOutInfo();

					try {
						Event event = queue.nextElement();
						if (event == null) {
							logger.error("Event is NULL.");
						} else {
							eventConsumer.push(event);
						}
					} catch (Exception e) {
						logger.error("Delivering event to consumer fail.", e);
					}
				} else {
					try {
						sleep(sleepTime);
					} catch (InterruptedException ignored) {
					}
				}
			}
		} catch (Throwable throwable) {
			try {
				logger.error("run() ", throwable);
			} catch (Exception e) {
				System.out.println(QueuedEventSender.class + " Can't log.");
				throwable.printStackTrace();
			}
		}
	}

	/**
	 * Get queue overflows count.
	 * 
	 * @return {@link Integer}
	 */
	public int getOverflowCount() {
		return overflowCount;
	}

	/**
	 * Get throw away events count.
	 * 
	 * @return {@link Integer}
	 */
	public int getThrowAwayCount() {
		return throwAwayCount;
	}

	/**
	 * Get statistic string.
	 * 
	 * @return {@link String}
	 */
	public String getStatsString() {
		return "EventsReceived:" + receivedCount + ", Queue: " + queue.toString() + ", OC:" + overflowCount + ", TAC:" + throwAwayCount;
	}

	/**
	 * Log statistic information.
	 */
	public void logOutInfo() {
		logger.info("ReceiverName:" + name + ", ChannelName:" + channelName + ", Stats[" + getStatsString() + "]");
	}

	/**
	 * Is queue has unsent elements.
	 * 
	 * @return {@link Boolean}
	 */
	public boolean hasUnsentElements() {
		return queue.hasElements();
	}

}
