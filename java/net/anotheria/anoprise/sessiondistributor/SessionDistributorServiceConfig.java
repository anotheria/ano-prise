package net.anotheria.anoprise.sessiondistributor;

import net.anotheria.util.TimeUnit;
import org.apache.log4j.Logger;
import org.configureme.ConfigurationManager;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;

/**
 * SessionDistributorServiceConfig as ConfigureMe config. Holds main configurable properties for SessionDistributorService.
 * <p>Allows to enable and disable clustering, set amount of nodes in the SD cluster, configure KeepAlive interval, enable disable failings, sessions persistence on shutdown....  so on.</p>
 *
 * @author h3ll
 */
@ConfigureMe(name = "ano-prise-session-distributor-service")
public class SessionDistributorServiceConfig {

	/**
	 * Default session distributor event channel queue size.
	 */
	protected static final int DEFAULT_SD_EVEN_CHANNEL_Q_SIZE = 5000;
	/**
	 * Default session distributor event channel queue sleep time.
	 */
	protected static final long DEFAULT_SD_EVEN_CHANNEL_Q_SLEEP_TIME = 300;


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

	/**
	 * Enables or disables SD clustering.
	 * Disabled by defaults!
	 */
	@Configure
	private boolean multipleInstancesEnabled;

	/**
	 * Amount of SessionDistributor services in the system.
	 * Actually MOD - for  routing.
	 * 1- by default! Means that clustering is disabled!
	 */
	@Configure
	private int sessionDistributorServersAmount;

	/**
	 * Disable/enable failing.
	 */
	@Configure
	private boolean failingStrategyEnabled;

	/**
	 * Disable/Enable sessions cache backup to FS on server shutDown/restart - etc.
	 */
	@Configure
	private boolean wrightSessionsToFsOnShutdownEnabled;

	/**
	 * Root folder on FS for SD sessions wright on shutdown.
	 */
	@Configure
	private String sdSessionsFSRootFolder;
	/**
	 * File extension for sessions file.
	 */
	@Configure
	private String sdSessionsFileExtension;

	/**
	 * Max size for SDCache event queue.
	 */
	@Configure
	private int sdCacheEventQueueSize;
	/**
	 * Sleep time for SDCache event queue.
	 */
	@Configure
	private long sdCacheEventQueueSleepTime;


	public static SessionDistributorServiceConfig getInstance() {
		return SessionDistributorConfigInstanceHolder.INSTANCE;
	}

	/**
	 * Private constructor with default values.
	 */
	private SessionDistributorServiceConfig() {
		this.distributedSessionsCleanUpInterval = TimeUnit.MINUTE.getMillis() * 5;// five minutes
		this.distributedSessionMaxAge = TimeUnit.MINUTE.getMillis() * 10; //ten minutes
		this.sessionDistributorEventQueueSize = DEFAULT_SD_EVEN_CHANNEL_Q_SIZE;
		this.sessionDistributorEventQueueSleepTime = DEFAULT_SD_EVEN_CHANNEL_Q_SLEEP_TIME;
		//off by default
		this.multipleInstancesEnabled = false;
		//off by default
		this.failingStrategyEnabled = false;
		this.sessionDistributorServersAmount = 1;
		//disabled by default
		this.wrightSessionsToFsOnShutdownEnabled = false;
		this.sdSessionsFSRootFolder = "sessionDistributorService_sessionsState";
		this.sdSessionsFileExtension = "sds";

		this.sdCacheEventQueueSize = DEFAULT_SD_EVEN_CHANNEL_Q_SIZE;
		this.sdCacheEventQueueSleepTime = DEFAULT_SD_EVEN_CHANNEL_Q_SLEEP_TIME;
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

	public boolean isMultipleInstancesEnabled() {
		return multipleInstancesEnabled;
	}

	public void setMultipleInstancesEnabled(boolean multipleInstancesEnabled) {
		this.multipleInstancesEnabled = multipleInstancesEnabled;
	}

	public int getSessionDistributorServersAmount() {
		return sessionDistributorServersAmount;
	}

	public void setSessionDistributorServersAmount(int sessionDistributorServersAmount) {
		//set only in case  when more then 1. Otherwise rely on defaults!
		this.sessionDistributorServersAmount = sessionDistributorServersAmount > 0 ? sessionDistributorServersAmount : 0;
	}

	public boolean isFailingStrategyEnabled() {
		return failingStrategyEnabled;
	}

	public void setFailingStrategyEnabled(boolean failingStrategyEnabled) {
		this.failingStrategyEnabled = failingStrategyEnabled;
	}

	public boolean isWrightSessionsToFsOnShutdownEnabled() {
		return wrightSessionsToFsOnShutdownEnabled;
	}

	public void setWrightSessionsToFsOnShutdownEnabled(boolean wrightSessionsToFsOnShutdownEnabled) {
		this.wrightSessionsToFsOnShutdownEnabled = wrightSessionsToFsOnShutdownEnabled;
	}

	public String getSdSessionsFSRootFolder() {
		return sdSessionsFSRootFolder;
	}

	public void setSdSessionsFSRootFolder(String sdSessionsFSRootFolder) {
		this.sdSessionsFSRootFolder = sdSessionsFSRootFolder;
	}

	public String getSdSessionsFileExtension() {
		return sdSessionsFileExtension;
	}

	public void setSdSessionsFileExtension(String sdSessionsFileExtension) {
		this.sdSessionsFileExtension = sdSessionsFileExtension;
	}

	public int getSdCacheEventQueueSize() {
		return sdCacheEventQueueSize;
	}

	public void setSdCacheEventQueueSize(int sdCacheEventQueueSize) {
		this.sdCacheEventQueueSize = sdCacheEventQueueSize;
	}

	public long getSdCacheEventQueueSleepTime() {
		return sdCacheEventQueueSleepTime;
	}

	public void setSdCacheEventQueueSleepTime(long sdCacheEventQueueSleepTime) {
		this.sdCacheEventQueueSleepTime = sdCacheEventQueueSleepTime;
	}

	/**
	 * Static SessionDistributorConfigInstanceHolder which holds configured SessionDistributorServiceConfig instance.
	 */
	private static class SessionDistributorConfigInstanceHolder {
		/**
		 * SessionDistributorServiceConfig instance.
		 */
		private static SessionDistributorServiceConfig INSTANCE;

		/**
		 * Static initialisation block.
		 */
		static {
			INSTANCE = new SessionDistributorServiceConfig();
			try {
				ConfigurationManager.INSTANCE.configure(INSTANCE);
			} catch (Exception e) {
				Logger.getLogger(SessionDistributorServiceConfig.class).error("SessionDistributorServiceConfig configuration failed. configuring defaults. " + e.getMessage());
			}
		}

	}
}
