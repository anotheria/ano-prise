package net.anotheria.anoprise.sessiondistributor;

import net.anotheria.util.TimeUnit;
import org.apache.log4j.Logger;
import org.configureme.ConfigurationManager;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;

/**
 *  SessionDistributorServiceConfig as ConfigureMe config. Holds main configurable properties for SessionDistributorService.
 *
 * @author h3ll
 */

/**
 *
 */
@ConfigureMe(name = "session-distributor-service")
public class SessionDistributorServiceConfig {

	/**
	 * Default session distributor event channel queue size.
	 */
	protected static final int DEFAULT_SESSION_DISTRIBUTOR_EVEN_CHANNEL_Q_SIZE = 5000;
	/**
	 * Default session distributor event channel queue sleep time.
	 */
	protected static final long DEFAULT_SESSION_DISTRIBUTOR_EVEN_CHANNEL_Q_SLEEP_TIME = 300;


	/**
	 * SessionDistributorServiceConfig 'distributedSessionsCleanUpInterval'.
	 * Represent interval of time on which session cleanUp task runs.
	 */
	@Configure
	private long distributedSessionsCleanUpInterval;

	/**
	 * SessionDistributorServiceConfig 'distributedSessionMaxAge'.
	 * Represent max time to live in mills for DistributedSessionVO.
	 */
	@Configure
	private long distributedSessionMaxAge;

	/**
	 * SessionDistributorServiceConfig 'sessionDistributorEventQueueSize'.
	 */
	@Configure
	private int sessionDistributorEventQueueSize;
	/**
	 * SessionDistributorServiceConfig 'sessionDistributorEventQueueSleepTime'.
	 */
	@Configure
	private long sessionDistributorEventQueueSleepTime;


	public static SessionDistributorServiceConfig getInstance() {
		return SessionDistributorConfigInstanceHolder.ServiceCONFIG_INSTANCE;
	}

	/**
	 * Private constructor with default values.
	 */
	private SessionDistributorServiceConfig() {
		this.distributedSessionsCleanUpInterval = TimeUnit.MINUTE.getMillis() * 5;// five minutes
		this.distributedSessionMaxAge = TimeUnit.MINUTE.getMillis() * 10; //ten minutes
		this.sessionDistributorEventQueueSize = DEFAULT_SESSION_DISTRIBUTOR_EVEN_CHANNEL_Q_SIZE;
		this.sessionDistributorEventQueueSleepTime = DEFAULT_SESSION_DISTRIBUTOR_EVEN_CHANNEL_Q_SLEEP_TIME;
	}

	public long getDistributedSessionMaxAge() {
		return distributedSessionMaxAge;
	}

	public void setDistributedSessionMaxAge(long distributedSessionMaxAge) {
		this.distributedSessionMaxAge = distributedSessionMaxAge;
	}

	public long getDistributedSessionsCleanUpInterval() {
		return distributedSessionsCleanUpInterval;
	}

	public void setDistributedSessionsCleanUpInterval(long distributedSessionsCleanUpInterval) {
		this.distributedSessionsCleanUpInterval = distributedSessionsCleanUpInterval;
	}

	public int getSessionDistributorEventQueueSize() {
		return sessionDistributorEventQueueSize;
	}

	public void setSessionDistributorEventQueueSize(int sessionDistributorEventQueueSize) {
		this.sessionDistributorEventQueueSize = sessionDistributorEventQueueSize;
	}

	public long getSessionDistributorEventQueueSleepTime() {
		return sessionDistributorEventQueueSleepTime;
	}

	public void setSessionDistributorEventQueueSleepTime(long sessionDistributorEventQueueSleepTime) {
		this.sessionDistributorEventQueueSleepTime = sessionDistributorEventQueueSleepTime;
	}

	/**
	 * Static SessionDistributorConfigInstanceHolder which holds configured SessionDistributorServiceConfig instance.
	 */
	private static class SessionDistributorConfigInstanceHolder {
		/**
		 * SessionDistributorServiceConfig instance.
		 */
		private static SessionDistributorServiceConfig ServiceCONFIG_INSTANCE;

		/**
		 * Static initialisation block.
		 */
		static {
			ServiceCONFIG_INSTANCE = new SessionDistributorServiceConfig();
			try {
				ConfigurationManager.INSTANCE.configure(ServiceCONFIG_INSTANCE);
			} catch (Exception e) {
				Logger.getLogger(SessionDistributorServiceConfig.class).error("SessionDistributorServiceConfig configuration failed. configuring defaults.",
						e);
			}
		}

	}
}
