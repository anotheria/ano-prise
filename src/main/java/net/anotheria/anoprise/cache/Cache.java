package net.anotheria.anoprise.cache;

import net.anotheria.moskito.core.predefined.CacheStats;

/**
 * An interface for the service side object cache.
 *
 * @param <K> key type.
 * @param <V> value type.
 *
 * @author another
 * @version $Id: $Id
 */
public interface Cache<K,V> {
	/**
	 * Returns the object for the given id from cache, null if no object is in cache.
	 *
	 * @param id the id to retrieve
	 * @return a V object.
	 */
	V get(K id);
	
	/**
	 * Puts the cacheable object in cache.
	 *
	 * @param id the object id
	 * @param cacheable the object to cache.
	 */
	void put(K id, V cacheable);

	/**
	 * Removes an object from the cache.
	 *
	 * @param id cache object id.
	 */
	void remove(K id);
	
	/**
	 * Clears the cache.
	 */
	void clear();

	/**
	 * Returns the cache stats.
	 *
	 * @return a {@link net.anotheria.moskito.core.predefined.CacheStats} object.
	 */
	CacheStats getCacheStats();
	
}
