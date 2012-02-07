package net.anotheria.anoprise.sessiondistributor.cache;

import net.anotheria.anoprise.fs.*;
import net.anotheria.anoprise.sessiondistributor.SessionDistributorServiceConfig;
import net.anotheria.util.StringUtils;
import org.apache.log4j.Logger;

/**
 * SDCacheUtil - provides FS persistence functionality for  {@link SDCache}.
 *
 * @author h3ll
 */
public final class SDCacheUtil {
	/**
	 * Logger.
	 */
	private static final Logger LOG = Logger.getLogger(SDCacheUtil.class);

	/**
	 * Prefix for LOG message.
	 */
	private static final String LOG_PREFIX = "SD_FS_PERSISTENCE: ";

	/**
	 * {@link net.anotheria.anoprise.fs.FSService} for storing service data.
	 */
	private static final FSService<SDCache> fsPersistence;

	/**
	 * Constructor.
	 */
	static {
		try {
			SessionDistributorServiceConfig serviceConfig = SessionDistributorServiceConfig.getInstance();
			FSServiceConfig config = new FSServiceConfig(serviceConfig.getSdSessionsFSRootFolder(), serviceConfig.getSdSessionsFileExtension());
			fsPersistence = FSServiceFactory.createFSService(config);
		} catch (FSServiceConfigException fSSce) {
			LOG.fatal(LOG_PREFIX + "Unable to initialize FSService. ", fSSce);
			throw new RuntimeException(fSSce);
		}
	}


	/**
	 * Read {@link SDCache}.
	 * <p/>
	 * Anyway - this method will return some Cache :)  so please do not add any NULL checks.
	 *
	 * @param ownerId actually owner id. (can be SDServiceId - or whatEver.... etc)
	 * @return {@link SDCache} read from FS - or newly created.
	 */
	public static SDCache createCache(String ownerId) {
		if (StringUtils.isEmpty(ownerId))
			throw new IllegalArgumentException("Invalid id passed");

		if (!SessionDistributorServiceConfig.getInstance().isWrightSessionsToFsOnShutdownEnabled())
			return new SDCache();
		try {
			SDCache cache = fsPersistence.read(ownerId);
			// remove  after read!
			try {
				fsPersistence.delete(ownerId);
			} catch (FSServiceException e) {
				LOG.warn("Deleting restored sessions cache  failed! READ : " + cache);
			}
			return cache;
		} catch (FSServiceException fSSe) {
			LOG.warn(LOG_PREFIX + " read(" + ownerId + ") failed - creating empty cache!", fSSe);
			return new SDCache();
		}
	}

	/**
	 * Save {@link SDCache}.
	 *
	 * @param sessionsCache {@link SDCache} to save
	 */
	public static void save(SDCache sessionsCache) {
		if (sessionsCache == null)
			throw new IllegalArgumentException("Invalid sessionsCache");
		try {
			fsPersistence.save(sessionsCache);
		} catch (FSServiceException e) {
			LOG.warn(LOG_PREFIX + " save(" + sessionsCache + ") failed!", e);
		}
	}
}

