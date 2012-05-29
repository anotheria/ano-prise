package net.anotheria.anoprise.cache;

/**
 * Defines a factory for caches.
 *
 * @param <K> cache key.
 * @param <V> cache value.
 * @author lrosenberg
 */
public interface CacheFactory<K, V> {
	/**
	 * Creates a new cache with given name, start and max size.
	 *
	 * @param name	  name of the cache.
	 * @param startSize start size of the cache.
	 * @param maxSize   max size of the cache.
	 * @return created cache object.
	 */
	Cache<K, V> create(String name, int startSize, int maxSize);

	/**
	 * Creates a new expiring cache with given name, start and max size, expiration time
	 *
	 * @param name		   name of the cache
	 * @param startSize	  start size of the cache
	 * @param maxSize		max size of the cache
	 * @param expirationTime expiration time of the cache elements
	 * @return created cache object
	 */
	ExpiringCache<K, V> createExpiried(String name, int startSize, int maxSize, long expirationTime);
}

