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
		return new RoundRobinHardwiredCache<K, V>(name, startSize, maxSize);
	}

	@Override
	public ExpiringCache<K, V> createExpiried(String name, int startSize, int maxSize, long expirationTime) {
		Cache<K, CachedObjectWrapper<V>> underlyingCache = new RoundRobinHardwiredCache<K, CachedObjectWrapper<V>>(name, startSize, maxSize);
		return new ExpiringCache<K, V>(name, expirationTime, underlyingCache);
	}

	@Override
	public Cache<K, V> createFailover(String name, int startSize, int maxSize, int serviceAmount, String registrationNameProvider) {
		throw new UnsupportedOperationException("method not supported");
	}

	@Override
	public ExpiringCache<K, V> createExpiriedFailover(String name, int startSize, int maxSize, long expirationTime, int serviceAmount, String registrationNameProvider) {
		throw new UnsupportedOperationException("method not supported");
	}

}
