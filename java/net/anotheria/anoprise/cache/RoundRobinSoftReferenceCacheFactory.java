package net.anotheria.anoprise.cache;

/**
 * Factory for the RoundRobinSoftReferenceCache.
 *
 * @param <K> type for the key.
 * @param <V> type for the value.
 * @author lrosenberg
 */
public class RoundRobinSoftReferenceCacheFactory<K, V> implements CacheFactory<K, V> {

	private ModableTypeHandler modableTypeHandler = null;

	public RoundRobinSoftReferenceCacheFactory() {
	}

	public RoundRobinSoftReferenceCacheFactory(ModableTypeHandler modableTypeHandler) {
		this.modableTypeHandler = modableTypeHandler;
	}

	@Override
	public Cache<K, V> create(String name, int startSize, int maxSize) {
		return new RoundRobinSoftReferenceCache<K, V>(name, startSize, maxSize);
	}

	@Override
	public ExpiringCache<K, V> createExpiried(String name, int startSize, int maxSize, long expirationTime) {
		Cache<K, CachedObjectWrapper<V>> underlyingCache = new RoundRobinSoftReferenceCache(name, startSize, maxSize);
		return new ExpiringCache<K, V>(name, expirationTime, underlyingCache);
	}

	@Override
	public Cache<K, V> createFailover(String name, int startSize, int maxSize, int serviceAmount, String registrationNameProvider) {
		return new RoundRobinSoftReferenceFailoverSupportCache<K, V>(name, startSize, maxSize, serviceAmount, registrationNameProvider, modableTypeHandler);
	}

	@Override
	public ExpiringCache<K, V> createExpiriedFailover(String name, int startSize, int maxSize, long expirationTime, int serviceAmount, String registrationNameProvider) {
		Cache<K, CachedObjectWrapper<V>> underlyingCache = new RoundRobinSoftReferenceFailoverSupportCache(name, startSize, maxSize, serviceAmount, registrationNameProvider, modableTypeHandler);
		return new ExpiringCache<K, V>(name, expirationTime, underlyingCache);
	}


}
