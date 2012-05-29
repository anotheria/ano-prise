package net.anotheria.anoprise.cache;

/**
 * Factory for the RoundRobinSoftReferenceCache.
 * @author lrosenberg
 *
 * @param <K> type for the key.
 * @param <V> type for the value.
 */
public class RoundRobinSoftReferenceCacheFactory<K,V> implements CacheFactory<K, V> {

	@Override
	public Cache<K, V> create(String name, int startSize, int maxSize) {
		return new RoundRobinSoftReferenceCache<K, V>(name, startSize, maxSize);
	}

	@Override
	public ExpiringCache<K, V> createExpiried(String name, int startSize, int maxSize, long expirationTime) {
		Cache<K, CachedObjectWrapper<V>> underlyingCache = new RoundRobinSoftReferenceCache(name, startSize, maxSize);
		return new ExpiringCache<K, V>(name, expirationTime, underlyingCache);
	}

}
