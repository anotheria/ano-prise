package net.anotheria.anoprise.cache;

/**
 * Factory for the RoundRobinHardwiredCache.
 *
 * @param <K> key type.
 * @param <V> value type.
 * @author lrosenberg
 */
public class RoundRobinHardwiredCacheFactory<K, V> implements CacheFactory<K, V> {

	@Override
	public Cache<K, V> create(String name, int startSize, int maxSize) {
		return new RoundRobinHardwiredCache<>(name, startSize, maxSize);
	}

	@Override
	public ExpiringCache<K, V> createExpiring(String name, int startSize, int maxSize, long expirationTime) {
		Cache<K, CachedObjectWrapper<V>> underlyingCache = new RoundRobinHardwiredCache<>(name, startSize, maxSize);
		return new ExpiringCache<>(name, expirationTime, underlyingCache);
	}
}
