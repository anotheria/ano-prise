package net.anotheria.anoprise.cache;

import net.anotheria.moskito.core.logging.DefaultStatsLogger;
import net.anotheria.moskito.core.logging.IntervalStatsLogger;
import net.anotheria.moskito.core.logging.Log4JOutput;
import net.anotheria.moskito.core.stats.DefaultIntervals;
import org.apache.log4j.Logger;
import org.configureme.ConfigurationManager;

/**
 * Utility for cache creation.
 *
 * @author lrosenberg
 */
public final class Caches {

	/**
	 * Creates a new softreference cache.
	 *
	 * @param <K>
	 * @param <V>
	 * @param name
	 * @return
	 */
	public static <K, V> Cache<K, V> createSoftReferenceCache(String name) {
		return new RoundRobinSoftReferenceCache<K, V>(name);
	}

	/**
	 * Creates a new soft reference cache with given name, start and max size.
	 *
	 * @param <K>       type used as key in the cache.
	 * @param <V>       type used as value in the cache.
	 * @param name	  name of the cache.
	 * @param startSize starting size of the cache.
	 * @param maxSize   max size of the cache.
	 * @return
	 */
	public static <K, V> Cache<K, V> createSoftReferenceCache(String name, int startSize, int maxSize) {
		return new RoundRobinSoftReferenceCache<K, V>(name, startSize, maxSize);
	}

	/**
	 * Creates a new hardwired cache with given name and default size.
	 * @param name
	 * @return
	 */
	public static <K, V> Cache<K, V> createHardwiredCache(String name) {
		return new RoundRobinHardwiredCache<K, V>(name);
	}

	/**
	 * Creates a new hardwired cache with given name and size.
	 * @param name
	 * @param startSize
	 * @param maxSize
	 * @return
	 */
	public static <K, V> Cache<K, V> createHardwiredCache(String name, int startSize, int maxSize) {
		return new RoundRobinHardwiredCache<K, V>(name, startSize, maxSize);
	}

	public static <K, V> Cache<K, V> createHardwiredExpiringCache(String name, int startSize, int maxSize, int expirationTime) {
		Cache<K, CachedObjectWrapper<V>> underlyingCache = createHardwiredCache(name, startSize, maxSize);
		return new ExpiringCache<K, V>(name, expirationTime, underlyingCache);
	}

	public static <K, V> Cache<K, V> createSoftReferenceExpiringCache(String name, int startSize, int maxSize, int expirationTime) {
		Cache<K, CachedObjectWrapper<V>> underlyingCache = createSoftReferenceCache(name, startSize, maxSize);
		return new ExpiringCache<K, V>(name, expirationTime, underlyingCache);
	}

	public static <K, V> Cache<K, V> createConfigurableSoftReferenceCache(String name) {
		CacheFactory<K, V> factory = new RoundRobinSoftReferenceCacheFactory<K, V>();
		CacheController<K, V> controller = new CacheController<K, V>(name, factory);
		ConfigurationManager.INSTANCE.configureAs(controller, name);
		return controller;
	}

	public static <K, V> Cache<K, V> createConfigurableSoftReferenceExpiringCache(String name) {
		CacheFactory<K, V> factory = new RoundRobinSoftReferenceCacheFactory<K, V>();
		CacheController<K, V> controller = new CacheController<K, V>(name, factory);
		ConfigurationManager.INSTANCE.configureAs(controller, name);
		return controller;
	}

	public static <K, V> Cache<K, V> createConfigurableHardwiredCache(String name) {
		CacheFactory<K, V> factory = new RoundRobinHardwiredCacheFactory<K, V>();
		CacheController<K, V> controller = new CacheController<K, V>(name, factory);
		ConfigurationManager.INSTANCE.configureAs(controller, name);
		return controller;
	}

	public static <K, V> Cache<K, V> createConfigurableCache(String name) {
		CacheController<K, V> controller = new CacheController<K, V>(name);
		ConfigurationManager.INSTANCE.configureAs(controller, name);
		return controller;
	}

	/**
	 * Creates a new soft reference cache with failover node support cache with given params
	 *
	 * @param name				  name of the cache.
	 * @param startSize			 starting size of the cache.
	 * @param maxSize			   max size of the cache.
	 * @param instanceAmount		number of the cache instances in order
	 * @param currentInstanceNumber current number of cache instance
	 * @param modableTypeHandler	instance of the ModableTypeHandler to calculate modable value, can be null if use primitive type the key in the cache.
	 * @param <K>                   type used as key in the cache.
	 * @param <V>                   type used as value in the cache.
	 * @return
	 */
	public static <K, V> Cache<K, V> createSoftReferenceFailoverSupportCache(String name, int startSize, int maxSize, int instanceAmount, int currentInstanceNumber, ModableTypeHandler modableTypeHandler) {
		Cache<K, V> underlyingCache = createSoftReferenceCache(name, startSize, maxSize);
		return new FailoverCache<K, V>(name, instanceAmount, currentInstanceNumber, modableTypeHandler, underlyingCache);
	}


	/**
	 * Creates a new soft reference expiring cache with failover node support cache with given params
	 *
	 * @param name				  name of the cache.
	 * @param startSize			 starting size of the cache.
	 * @param maxSize			   max size of the cache.
	 * @param expirationTime		expiration time of the cache entry
	 * @param instanceAmount		number of the cache instances in order
	 * @param currentInstanceNumber current number of cache instance
	 * @param modableTypeHandler	instance of the ModableTypeHandler to calculate modable value, can be null if use primitive type the key in the cache.
	 * @param <K>                   type used as key in the cache.
	 * @param <V>                   type used as value in the cache.
	 * @return created cahe
	 */

	public static <K, V> Cache<K, V> createSoftReferenceExpiringFailoverSupportCache(String name, int startSize, int maxSize, int expirationTime, int instanceAmount, int currentInstanceNumber, ModableTypeHandler modableTypeHandler) {
		Cache<K, V> underlyingCache = createSoftReferenceExpiringCache(name, startSize, maxSize, expirationTime);
		return new FailoverCache<K, V>(name, instanceAmount, currentInstanceNumber, modableTypeHandler, underlyingCache);
	}

	/**
	 * Creates a new configurable soft reference cache with failover node support cache with given params
	 *
	 * @param name			   configurable params (json file)
	 * @param modableTypeHandler instance of the ModableTypeHandler to calculate modable value, can be null if use primitive type the key in the cache.
	 * @param <K>                ype used as key in the cache.
	 * @param <V>                type used as value in the cache.
	 * @return
	 */
	public static <K, V> Cache<K, V> createConfigurableSoftReferenceCacheFailoverSupportCache(String name, int instanceAmount, int currentInstanceNumber, ModableTypeHandler modableTypeHandler) {
		CacheFactory<K, V> factory = new RoundRobinSoftReferenceCacheFactory<K, V>();
		CacheController<K, V> controller = new CacheController<K, V>(name, factory, instanceAmount, currentInstanceNumber, modableTypeHandler);
		ConfigurationManager.INSTANCE.configureAs(controller, name);
		return controller;
	}


	/**
	 * Creates a new configurable soft reference expiring cache with failover node support cache with given params
	 *
	 * @param name			   configurable params (json file)
	 * @param modableTypeHandler instance of the ModableTypeHandler to calculate modable value, can be null if use primitive type the key in the cache.
	 * @param <K>                ype used as key in the cache.
	 * @param <V>                type used as value in the cache.
	 * @return
	 */
	public static <K, V> Cache<K, V> createConfigurableSoftReferenceExpiringCacheFailoverSupportCache(String name, int instanceAmount, int currentInstanceNumber, ModableTypeHandler modableTypeHandler) {
		CacheFactory<K, V> factory = new RoundRobinSoftReferenceCacheFactory<K, V>();
		CacheController<K, V> controller = new CacheController<K, V>(name, factory, instanceAmount, currentInstanceNumber, modableTypeHandler);
		ConfigurationManager.INSTANCE.configureAs(controller, name);
		return controller;
	}

	public static void attachCacheToMoskitoLoggers(Cache<?, ?> cache, String producerId, String category, String subsystem) {
		CacheProducerWrapper cacheWrapper = new CacheProducerWrapper(cache, producerId, category, subsystem);
		new DefaultStatsLogger(cacheWrapper, new Log4JOutput(Logger.getLogger("moskito.custom.default")));
		new IntervalStatsLogger(cacheWrapper, DefaultIntervals.FIVE_MINUTES, new Log4JOutput(Logger.getLogger("moskito.custom.5m")));
		new IntervalStatsLogger(cacheWrapper, DefaultIntervals.FIFTEEN_MINUTES, new Log4JOutput(Logger.getLogger("moskito.custom.15m")));
		new IntervalStatsLogger(cacheWrapper, DefaultIntervals.ONE_HOUR, new Log4JOutput(Logger.getLogger("moskito.custom.1h")));
		new IntervalStatsLogger(cacheWrapper, DefaultIntervals.ONE_DAY, new Log4JOutput(Logger.getLogger("moskito.custom.1d")));
	}

	/*
	public static final <K,V> Cache<K,V> createConfigurableHardwiredExpiringCache(String name){
		Cache<K,CachedObjectWrapper<V>> underlyingCache = createHardwiredCache(name, startSize, maxSize);
		return new ExpiringCache<K, V>(name, expirationTime, underlyingCache);
	}
	
	public static final <K,V> Cache<K,V> createConfigurableSoftReferenceExpiringCache(String name){
		Cache<K,CachedObjectWrapper<V>> underlyingCache = createSoftReferenceCache(name, startSize, maxSize);
		return new ExpiringCache<K, V>(name, expirationTime, underlyingCache);
	}
	
	*/


	private Caches() {
		//protect from instantiation.
	}

	public static enum Strategy {
		ROUNDROBIN,
		EXPIRATION
	}

	public static enum Wiring {
		HARDWIRED,
		SOFTREFERENCE,
	}
}
