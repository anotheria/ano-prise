package net.anotheria.anoprise.cache;

import net.anotheria.moskito.core.predefined.CacheStats;

/**
 * A cache implementation where elements expire after some time.
 * @author another
 *
 * @param <K> key class.
 * @param <V> value class.
 */
public class ExpiringCache<K,V> implements Cache<K,V>{
	/**
	 * Expiration time for cache entries to become invalid.
	 */
	private long expirationTime;
	
	/**
	 * Internal cache delegate.
	 */
	private Cache<K, CachedObjectWrapper<V>> cache;
	/**
	 * Moskito cache stats of the internal cache delegate. Both caches work on the same cache stats object ( but different values of it).
	 */
	private CacheStats moskitoCacheStats;
	
	public ExpiringCache(String name, int aStartSize, int aMaxSize, long anExpirationTime, CacheFactory<K, CachedObjectWrapper<V>> underlyingCacheFactory){
		this(name, anExpirationTime, underlyingCacheFactory.create(name, aStartSize, aMaxSize));
	}

	public ExpiringCache(String name, long anExpirationTime, Cache<K, CachedObjectWrapper<V>> underlyingCache){
		cache = underlyingCache;
		expirationTime = anExpirationTime;
		moskitoCacheStats = cache.getCacheStats();
	}
	
	
	@Override public V get(K id) {
		CachedObjectWrapper<V> wrapper = cache.get(id);
		if (wrapper==null)
			return null;
		long now = System.currentTimeMillis();
		
		
		if (now-wrapper.getTimestamp()>expirationTime){
			moskitoCacheStats.addExpired();
			remove(id);
			return null;
		}
		
		return wrapper.getObj();
	}

	public void put(K id, V cacheable) {
		cache.put(id, new CachedObjectWrapper<V>(cacheable));
	}
	
	@Override public String toString(){
		return cache.toString()+", ExpirationTime: "+expirationTime;
	}
	
	@Override public void clear() {
		cache.clear();
	}

	@Override public void remove(K id) {
		cache.remove(id);
	}

	@Override public CacheStats getCacheStats() {
		return moskitoCacheStats;
	}
	
	void setExpirationTime(long anExpirationTime){
		expirationTime = anExpirationTime;
	}
}
