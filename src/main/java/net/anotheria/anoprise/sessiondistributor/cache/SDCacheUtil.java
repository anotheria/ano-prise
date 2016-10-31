package net.anotheria.anoprise.sessiondistributor.cache;

import net.anotheria.anoprise.fs.FSService;
import net.anotheria.anoprise.fs.FSServiceConfig;
import net.anotheria.anoprise.fs.FSServiceConfigException;
import net.anotheria.anoprise.fs.FSServiceException;
import net.anotheria.anoprise.fs.FSServiceFactory;
import net.anotheria.anoprise.sessiondistributor.SessionDistributorServiceConfig;
import net.anotheria.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * SDCacheUtil - provides FS persistence functionality for  {@link net.anotheria.anoprise.sessiondistributor.cache.SDCache}.
 *
 * @author h3ll
 * @version $Id: $Id
 */
public final class SDCacheUtil {

	/**
	 * Fatal marker for logback.
	 */
	private static final Marker FATAL = MarkerFactory.getMarker("FATAL");
	/**
	 * Logger.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(SDCacheUtil.class);

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
			LOG.error(FATAL, LOG_PREFIX + "Unable to initialize FSService. ", fSSce);
			throw new RuntimeException(fSSce);
		}
	}


	/**
	 * Read {@link net.anotheria.anoprise.sessiondistributor.cache.SDCache}.
	 * <p/>
	 * Anyway - this method will return some Cache :)  so please do not add any NULL checks.
	 *
	 * @return {@link net.anotheria.anoprise.sessiondistributor.cache.SDCache} read from FS - or newly created.
	 */
	public static SDCache createCache() {
		if (!SessionDistributorServiceConfig.getInstance().isWrightSessionsToFsOnShutdownEnabled())
			return new SDCache();

		//checking if Node ID  was provided via SystemProperty , or default should be used!
		String cacheId = System.getProperty(SessionDistributorServiceConfig.getInstance().getNodeIdSystemPropertyName());
		cacheId = StringUtils.isEmpty(cacheId) ? SDCache.DEFAULT_NODE_ID : cacheId;
		try {
			SDCache cache = fsPersistence.read(cacheId);
			// remove  after read!
			try {
				fsPersistence.delete(cacheId);
			} catch (FSServiceException e) {
				LOG.warn(LOG_PREFIX + "Deleting restored sessions cache  failed! CAUSE : " + e.getMessage());
			}
			return cache;
		} catch (FSServiceException fSSe) {
			LOG.warn(LOG_PREFIX + " read(" + cacheId + ") failed - creating empty cache!", fSSe);
			return new SDCache();
		}
	}

	/**
	 * Save {@link net.anotheria.anoprise.sessiondistributor.cache.SDCache}.
	 *
	 * @param sessionsCache {@link net.anotheria.anoprise.sessiondistributor.cache.SDCache} to save
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

