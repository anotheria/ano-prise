package net.anotheria.anoprise.cache;

import net.anotheria.moskito.core.predefined.CacheStats;
import org.configureme.annotations.AfterConfiguration;
import org.configureme.annotations.BeforeConfiguration;
import org.configureme.annotations.BeforeInitialConfiguration;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;


/**
 * This class is a cache controller, which hides access to a configurable
 * cache, so the service itself do not need to check the configuration.
 * If the cache is on, the calls will be delegated to the cache, otherwise
 * they will be directly answered (returning null and puting nothing).
 * Currently RoundRobinCache is used as internal cache delegate.
 *
 * @author lrosenberg
 *         Created on 29.07.2004
 */
@ConfigureMe
public class CacheController<K, V> implements Cache<K, V> {

	/**
	 * Fatal marker for logback.
	 */
	private static final Marker FATAL = MarkerFactory.getMarker("FATAL");

	/**
	 * If true the cache is on.
	 */
	@Configure
	private boolean cacheOn;
	/**
	 * Cache start size.
	 */
	@Configure
	private int startSize;
	/**
	 * Cache max size.
	 */
	@Configure
	private int maxSize;

	/**
	 * Class for the configuration factory.
	 */
	@Configure
	private String factoryClazz;

	/**
	 * Expiration time for cache entries to become invalid, if we use ExpiringCache
	 */
	@Configure
	private long expirationTime;


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
	 * instanceAmount previous to a reconfigur
	 */
	private int preInstanceAmount;

	/**
	 * currentInstanceNumber previous to a reconfigur
	 */
	private int preCurrentInstanceNumber;

	/**
	 * Underlying cache.
	 */
	private Cache<K, V> cache;

	/**
	 * Name of the cache configuration.
	 */
	private String configurationName;

	/**
	 * Type handler used for failover cache
	 */
	private ModableTypeHandler typeHandler;

	/**
	 * instanceAmount for failover support cache - amount for nodes
	 */
	private int instanceAmount = 1;

	/**
	 * currentInstanceNumber for failover support cache - extension to get current mode number
	 */
	private int currentInstanceNumber = 0;


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
	 * Initial value for the instanceAmount
	 */
	public static final int DEF_INSTANCE_AMOUNT = 1;

	/**
	 * Initial value for the currentInstanceNumber.
	 */
	public static final int DEF_CURRENT_INSTANCE_NUMBER = -1;

	/**
	 * Logger.
	 */
	private static Logger log = LoggerFactory.getLogger(CacheController.class);

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
	 * Creates a new CacheController with a configuration name and a cache factory.
	 *
	 * @param aConfigurationName the name to configure with.
	 * @param aFactory		   the factory to create new cache instances.
	 * @param aTypeHandler	   type handler used for failover cache
	 */
	public CacheController(String aConfigurationName, CacheFactory<K, V> aFactory, int aInstanceAmount, int aCurrentInstanceNumber, ModableTypeHandler aTypeHandler) {
		this(aConfigurationName, aFactory);
		typeHandler = aTypeHandler;
		instanceAmount = aInstanceAmount;
		currentInstanceNumber = aCurrentInstanceNumber;
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
				factory = (CacheFactory<K, V>) Class.forName(factoryClazz).newInstance();
			} catch (ClassNotFoundException e) {
				log.error(FATAL, "can't init cache", e);
				throw new AssertionError("Unproperly configured factory: " + factoryClazz + " --> " + e.getMessage(), e);
			} catch (InstantiationException e) {
				log.error(FATAL, "can't init cache", e);
				throw new AssertionError("Unproperly configured factory: " + factoryClazz + " --> " + e.getMessage(), e);
			} catch (IllegalAccessException e) {
				log.error(FATAL, "can't init cache", e);
				throw new AssertionError("Unproperly configured factory: " + factoryClazz + " --> " + e.getMessage(), e);
			}
		}

        log.debug("reiniting cache for {}", configurationName);
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
//					if (expirationTime == 0)
//						cache = factory.create(configurationName, startSize, maxSize);
//					else
//						cache = createExpiringCache(startSize, maxSize, expirationTime);
					cache = createCaches();
				}
			} else {
				log.debug("switching cache on.");
//				if (expirationTime == DEF_EXPIRATION_TIME)
//					cache = createCache(startSize, maxSize);
//				else
//					cache = createExpiringCache(startSize, maxSize, expirationTime);
				cache = createCaches();
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
		return factory.createExpiring(configurationName, aStartSize, aMaxSize, expirationTime);
	}

	protected Cache<K, V> createCacheFailover(int aStartSize, int aMaxSize) {
		if (factory == null)
			throw new IllegalStateException("No factory is configured or submitted for cache creation!");
		Cache<K, V> underlyingCache = factory.create(configurationName, aStartSize, aMaxSize);
		return new FailoverCache<>(instanceAmount, currentInstanceNumber, typeHandler, underlyingCache);
	}

	protected Cache<K, V> createExpiringCacheFailover(int aStartSize, int aMaxSize, long expirationTime) {
		if (factory == null)
			throw new IllegalStateException("No factory is configured or submitted for cache creation!");
		Cache<K, V> underlyingCache = factory.createExpiring(configurationName, aStartSize, aMaxSize, expirationTime);

		return new FailoverCache<>(aStartSize, aMaxSize, typeHandler, underlyingCache);
	}

	/**
	 * Create Cache base on params that were configured
	 *
	 * @return created cache
	 */
	protected Cache<K, V> createCaches() {
		if (instanceAmount <= DEF_INSTANCE_AMOUNT || currentInstanceNumber <= DEF_CURRENT_INSTANCE_NUMBER) {
			if (expirationTime == DEF_EXPIRATION_TIME)
				return createCache(startSize, maxSize);
			else
				return createExpiringCache(startSize, maxSize, expirationTime);
		} else {
			if (expirationTime == DEF_EXPIRATION_TIME)
				return createCacheFailover(startSize, maxSize);
			else
				return createExpiringCacheFailover(startSize, maxSize, expirationTime);
		}
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
        log.info("configuration {} finished, settings are:", configurationName);
        log.info("cacheOn {} -> {}", prevCacheOn, cacheOn);
        log.info("startSize {} -> {}", prevStartSize, startSize);
        log.info("maxSize {} -> {}", prevMaxSize, maxSize);
        log.info("expirationTime {} -> {}", preExpirationTime, expirationTime);
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
			stats += ' ' + cache.getCacheStats().toString() + ", OOME: " + outOfMemoryErrors;
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
		return cache.getCacheStats();
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

	public int getInstanceAmount() {
		return instanceAmount;
	}

	public void setInstanceAmount(int instanceAmount) {
		this.instanceAmount = instanceAmount;
	}

	public int getCurrentInstanceNumber() {
		return currentInstanceNumber;
	}

	public void setCurrentInstanceNumber(int currentInstanceNumber) {
		this.currentInstanceNumber = currentInstanceNumber;
	}
}
