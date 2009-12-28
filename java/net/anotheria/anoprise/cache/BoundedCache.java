package net.anotheria.anoprise.cache;

import net.java.dev.moskito.core.predefined.CacheStats;

/**
 * Interface for caches with limited size.
 * @author lrosenberg.
 *
 * @param <K> cache key.
 * @param <V> cache value.
 */
public interface BoundedCache<K,V> {
	/**
	 * Returns the object for the given id from cache, null if no object is in cache.
	 * @param id the id to retrieve
	 * @return
	 */
	V get(K id);
	
	/**
	 * Tries to put the cacheable object in cache.
	 * @param id the object id
	 * @param cacheable the object to cache.
	 * @return returns true if the attempt was successful or false if the cache was full.
	 */
	boolean offer(K id, V cacheable);

	void remove(K id);
	
	/**
	 * Clears the cache.
	 *
	 */
	void clear();

	CacheStats getCacheStats();
}
