package net.anotheria.anoprise.cache;

import net.anotheria.moskito.core.predefined.CacheStats;

/**
 * A cache implementation where elements stored based on the instance of cache
 * If its called by its instance (so current call is not fail over call) - element will be stored, other wise - not stored.
 *
 * @param <K> key value
 * @param <V> stored element
 * @author ivanbatura
 * @since 23.06.12
 */
public class FailoverCache<K, V> implements Cache<K, V> {
	/**
	 * Internal cache delegate.
	 */
	private Cache<K, V> cache;
	/**
	 * Total number of cache instance to failover support
	 */
	private int instanceAmount;
	/**
	 * Current number of cache instance to failover support
	 */
	private int currentInstanceNumber;
	/**
	 * modable value calculator
	 */
	private ModableTypeHandler modableTypeHandler;
	/**
	 * Moskito cache stats of the internal cache delegate. Both caches work on the same cache stats object ( but different values of it).
	 */
	private CacheStats moskitoCacheStats;

	public FailoverCache(String name, int aStartSize, int aMaxSize, int aInstanceAmount, int aCurrentInstanceNumber, ModableTypeHandler aModableTypeHandler, CacheFactory<K, V> underlyingCacheFactory) {
		this(name, aInstanceAmount, aCurrentInstanceNumber, aModableTypeHandler, underlyingCacheFactory.create(name, aStartSize, aMaxSize));
	}

	public FailoverCache(String name, int aInstanceAmount, int aCurrentInstanceNumber, ModableTypeHandler aModableTypeHandler, Cache<K, V> underlyingCache) {
		cache = underlyingCache;
		instanceAmount = aInstanceAmount > 0 ? aInstanceAmount : 1;
		currentInstanceNumber = aCurrentInstanceNumber;
		modableTypeHandler = aModableTypeHandler != null ? aModableTypeHandler : new DefaultModableTypeHandler();
		moskitoCacheStats = cache.getCacheStats();
	}


	@Override
	public V get(K id) {
		if (isFailOverCall(id))
			return null;
		return cache.get(id);
	}

	public void put(K id, V cacheable) {
		if (!isFailOverCall(id))
			cache.put(id, cacheable);
	}

	@Override
	public String toString() {
		return cache.toString() + ", instanceAmount=" + instanceAmount + ", currentInstanceNumber=" + currentInstanceNumber;
	}

	@Override
	public void clear() {
		cache.clear();
	}

	@Override
	public void remove(K id) {
		if (!isFailOverCall(id))
			cache.remove(id);
	}

	@Override
	public CacheStats getCacheStats() {
		return moskitoCacheStats;
	}

	/**
	 * Detect if this call was for failover or not
	 *
	 * @param id cache key to stored
	 */
	private boolean isFailOverCall(Object id) {
		if (instanceAmount < 2 || currentInstanceNumber < 0)
			return false;
		return currentInstanceNumber != (Math.abs(modableTypeHandler.getModableValue(id)) % instanceAmount);
	}

}
