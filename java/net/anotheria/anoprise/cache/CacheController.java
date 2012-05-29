package net.anotheria.anoprise.cache;

import net.java.dev.moskito.core.predefined.CacheStats;

import org.apache.log4j.Logger;
import org.configureme.annotations.AfterConfiguration;
import org.configureme.annotations.BeforeConfiguration;
import org.configureme.annotations.BeforeInitialConfiguration;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;


/**
 * This class is a cache controller, which hides access to a configurable
 * cache, so the service itself do not need to check the configuration.
 * If the cache is on, the calls will be delegated to the cache, otherwise
 * they will be directly answered (returning null and puting nothing).
 * Currently RoundRobinCache is used as internal cache delegate.
 * @author lrosenberg
 * Created on 29.07.2004
 */
@ConfigureMe
public class CacheController<K,V> implements Cache<K,V>{
	/**
	 * If true the cache is on.
	 */
	@Configure private boolean cacheOn;
	/**
	 * Cache start size.
	 */
	@Configure private int startSize;
	/**
	 * Cache max size.
	 */
	@Configure private int maxSize;

	/**
	 * Class for the configuration factory.
	 */
	@Configure private String factoryClazz;

	/**
	 * Expiration time for cache entries to become invalid, if we use ExpiringCache
	 */
	@Configure private long expirationTime;

	/**
	 * CacheOn value previous to a reconfigure.
	 */
	private boolean prevCacheOn;
	/**
	 * Start size previous to a reconfigure.
	 */
	private int prevStartSize;
	/**
	 * Max size previous to a reconfigure.
	 */
	private int prevMaxSize;

	/**
	 * Expiration time previous to a reconfigure.
	 */
	private long preExpirationTime;

	/**
	 * Underlying cache.
	 */
	private Cache<K,V> cache;

	/**
	 * Name of the cache configuration.
	 */
	private String configurationName;

	/**
	 * Number of out of memory errors in the underlying cache.
	 */
	private int outOfMemoryErrors;

	/**
	 * Initial value for cacheOn.
	 */
	public static final boolean DEF_CACHE_ON = true;
	/**
	 * Initial value for start size.
	 */
	public static final int DEF_START_SIZE = 1000;
	/**
	 * Initial value for the max size.
	 */
	public static final int DEF_MAX_SIZE = 5000;
	/**
	 * Initial value for the expiration time 0 - never expired.
	 */
	public static final int DEF_EXPIRATION_TIME = 0;


	/**
	 * Logger.
	 */
	private static Logger log = Logger.getLogger(CacheController.class);

	/**
	 * Factory for underlying cache creation.
	 */
	private CacheFactory<K, V> factory;

	/**
	 * Creates a new CacheController with a configuration name and a cache factory.
	 *
	 * @param aConfigurationName the name to configure with.
	 * @param aFactory		   the factory to create new cache instances.
	 */
	public CacheController(String aConfigurationName, CacheFactory<K, V> aFactory) {
		this.configurationName = aConfigurationName;
		factory = aFactory;
	}

	/**
	 * Creates a new CacheController with a configuration name and a cache factory.
	 *
	 * @param aConfigurationName the name to configure with.
	 */
	public CacheController(String aConfigurationName) {
		this(aConfigurationName, null);
	}

	/**
	 * Called before first configuration.
	 */
	@BeforeInitialConfiguration
	public void preInit() {
		prevCacheOn = false;
		prevStartSize = -1;
		prevMaxSize = -1;
		preExpirationTime = -1;
	}

	private void init() {

		if (factory == null) {
			try {
				@SuppressWarnings("unchecked")
				CacheFactory<K, V> newFactory = (CacheFactory<K, V>) Class.forName(factoryClazz).newInstance();
				factory = newFactory;
			} catch (ClassNotFoundException e) {
				log.fatal("can't init cache", e);
				throw new AssertionError("Unproperly configured factory: " + factoryClazz + " --> " + e.getMessage());
			} catch (InstantiationException e) {
				log.fatal("can't init cache", e);
				throw new AssertionError("Unproperly configured factory: " + factoryClazz + " --> " + e.getMessage());
			} catch (IllegalAccessException e) {
				log.fatal("can't init cache", e);
				throw new AssertionError("Unproperly configured factory: " + factoryClazz + " --> " + e.getMessage());
			}
		}

		log.debug("reiniting cache for " + configurationName);
		if (!cacheOn) {
			if (prevCacheOn) {
				log.debug("switching cache off.");
				cache.clear();
				cache = null;
			} else {
				log.debug("cache remains off.");
			}
		} else {
			if (prevCacheOn) {
				if (prevMaxSize == maxSize && prevStartSize == startSize && preExpirationTime == expirationTime) {
					log.debug("Cache remains on, settings unchanged.");
				} else {
					log.debug("Cache remains on, settings changed, cache will be renewed.");
					if (cache != null) {
						cache.clear();
					} else {
						log.warn("Cache is null, when it shouldn't be.");
					}
					if (factory == null)
						throw new IllegalStateException("No factory is configured or submitted for cache creation!");
					if (expirationTime == 0)
						cache = factory.create(configurationName, startSize, maxSize);
					else
						cache = createExpiringCache(startSize, maxSize, expirationTime);
				}
			} else {
				log.debug("switching cache on.");
				if (expirationTime == DEF_EXPIRATION_TIME)
					cache = createCache(startSize, maxSize);
				else
					cache = createExpiringCache(startSize, maxSize, expirationTime);
			}
		}
	}

	protected Cache<K, V> createCache(int aStartSize, int aMaxSize) {
		if (factory == null)
			throw new IllegalStateException("No factory is configured or submitted for cache creation!");
		return factory.create(configurationName, aStartSize, aMaxSize);
	}

	protected Cache<K, V> createExpiringCache(int aStartSize, int aMaxSize, long expirationTime) {
		if (factory == null)
			throw new IllegalStateException("No factory is configured or submitted for cache creation!");
		return factory.createExpiried(configurationName, aStartSize, aMaxSize, expirationTime);
	}

	@Override
	public void clear() {
		if (cacheOn)
			cache.clear();
	}

	@Override
	public V get(K id) {
		if (!cacheOn)
			return null;
		return cache.get(id);
	}

	@Override
	public void put(K id, V cacheable) {
		if (!cacheOn)
			return;
		try {
			cache.put(id, cacheable);
		} catch (OutOfMemoryError error) {
			outOfMemoryErrors++;
			throw error;
		}
	}

	@Override
	public void remove(K id) {
		if (!cacheOn)
			return;
		cache.remove(id);
	}

	@AfterConfiguration
	public void configurationFinished() {
		log.info("configuration " + configurationName + " finished, settings are:");
		log.info("cacheOn " + prevCacheOn + " -> " + cacheOn);
		log.info("startSize " + prevStartSize + " -> " + startSize);
		log.info("maxSize " + prevMaxSize + " -> " + maxSize);
		log.info("expirationTime " + preExpirationTime + " -> " + expirationTime);
		init();
	}

	@BeforeConfiguration
	public void configurationStarted() {
		prevCacheOn = cacheOn;
		prevMaxSize = maxSize;
		prevStartSize = startSize;
		preExpirationTime = expirationTime;
		cacheOn = DEF_CACHE_ON;
		startSize = DEF_START_SIZE;
		maxSize = DEF_MAX_SIZE;
		expirationTime = DEF_EXPIRATION_TIME;
	}

	public String getStats() {
		String stats = cacheOn ? "On, " + startSize + ", " + maxSize : "Off";
		if (cacheOn)
			stats += " " + cache.getCacheStats().toString() + ", OOME: " + outOfMemoryErrors;
		return stats;
	}

	public String getDetails() {
		if (!cacheOn)
			return "off";
		return cache.toString();
	}


	protected Cache<K, V> getCache() {
		return cache;
	}

	@Override
	public CacheStats getCacheStats() {
		if (!cacheOn)
			return new CacheStats();
		CacheStats stats = getCache().getCacheStats();
		return stats;
	}

	public void setCacheOn(boolean cacheOn) {
		this.cacheOn = cacheOn;
	}

	public void setStartSize(int startSize) {
		this.startSize = startSize;
	}

	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

	protected boolean isCacheOn() {
		return cacheOn;
	}

	protected int getStartSize() {
		return startSize;
	}

	protected int getMaxSize() {
		return maxSize;
	}

	public void setFactoryClazz(String factoryClazz) {
		this.factoryClazz = factoryClazz;
	}

	public long getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(long expirationTime) {
		this.expirationTime = expirationTime;
	}
}
