package net.anotheria.anoprise.cache;

public class RoundRobinSoftReferenceCacheFactory<K,V> implements CacheFactory<K, V> {

	@Override
	public Cache<K, V> create(String name, int startSize, int maxSize) {
		return new RoundRobinSoftReferenceCache<K, V>(name, startSize, maxSize);
	}
	
}
