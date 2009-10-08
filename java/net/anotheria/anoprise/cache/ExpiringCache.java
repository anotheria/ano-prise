package net.anotheria.anoprise.cache;

import net.java.dev.moskito.core.predefined.CacheStats;


public class ExpiringCache<K,V> implements Cache<K,V>{


	private long expirationTime;
	
	
	private Cache<K, CachedObjectWrapper<V>> cache;
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
