package net.anotheria.anoprise.cache;

/**
 * Factory for the RoundRobinSoftReferenceCache.
 *
 * @param <K> type for the key.
 * @param <V> type for the value.
 * @author lrosenberg
 */
public class RoundRobinSoftReferenceCacheFactory<K, V> implements CacheFactory<K, V> {

	@Override
	public Cache<K, V> create(String name, int startSize, int maxSize) {
		return new RoundRobinSoftReferenceCache<>(name, startSize, maxSize);
	}

	@Override
	public ExpiringCache<K, V> createExpiring(String name, int startSize, int maxSize, long expirationTime) {
		Cache<K, CachedObjectWrapper<V>> underlyingCache = new RoundRobinSoftReferenceCache(name, startSize, maxSize);
		return new ExpiringCache<>(name, expirationTime, underlyingCache);
	}
}
