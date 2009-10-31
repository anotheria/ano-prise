package net.anotheria.anoprise.cache;

import org.configureme.ConfigurationManager;

/**
 * Utility for cache creation.
 * @author another
 *
 */
public final class Caches {

	/**
	 * Creates a new softreference cache.
	 * @param <K>
	 * @param <V>
	 * @param name
	 * @return
	 */
	public static final <K,V> Cache<K,V> createSoftReferenceCache(String name){
		return new RoundRobinSoftReferenceCache<K, V>(name);
	}
	
	/**
	 * Creates a new soft reference cache with given name, start and max size.
	 * @param <K> type used as key in the cache.
	 * @param <V> type used as value in the cache.
	 * @param name name of the cache.
	 * @param startSize starting size of the cache.
	 * @param maxSize max size of the cache.
	 * @return
	 */
	public static final <K,V> Cache<K,V> createSoftReferenceCache(String name, int startSize, int maxSize){
		return new RoundRobinSoftReferenceCache<K, V>(name, startSize, maxSize);
	}
	
	public static final <K,V> Cache<K,V> createHardwiredCache(String name){
		return new RoundRobinHardwiredCache<K, V>(name);
	}
	
	public static final <K,V> Cache<K,V> createHardwiredCache(String name, int startSize, int maxSize){
		return new RoundRobinHardwiredCache<K, V>(name, startSize, maxSize);
	}
	
	public static final <K,V> Cache<K,V> createHardwiredExpiringCache(String name, int startSize, int maxSize, int expirationTime){
		Cache<K,CachedObjectWrapper<V>> underlyingCache = createHardwiredCache(name, startSize, maxSize);
		return new ExpiringCache<K, V>(name, expirationTime, underlyingCache);
	}
	
	public static final <K,V> Cache<K,V> createSoftReferenceExpiringCache(String name, int startSize, int maxSize, int expirationTime){
		Cache<K,CachedObjectWrapper<V>> underlyingCache = createSoftReferenceCache(name, startSize, maxSize);
		return new ExpiringCache<K, V>(name, expirationTime, underlyingCache);
	}
	
	public static final <K,V> Cache<K,V> createConfigurableSoftReferenceCache(String name){
		CacheFactory<K, V> factory = new RoundRobinSoftReferenceCacheFactory<K, V>();
		CacheController<K, V> controller = new CacheController<K, V>(name, factory);
		ConfigurationManager.INSTANCE.configureAs(controller, name);
		return controller;
	}
	
	public static final <K,V> Cache<K,V> createConfigurableHardwiredCache(String name){
		CacheFactory<K, V> factory = new RoundRobinHardwiredCacheFactory<K, V>();
		CacheController<K, V> controller = new CacheController<K, V>(name, factory);
		ConfigurationManager.INSTANCE.configureAs(controller, name);
		return controller;
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
	
	
	
	
	
	
	private Caches(){
		//protect from instantiation.
	}
	
/*	public static enum Strategy{
		ROUNDROBIN,
		EXPIRATION
	}*/
}
