package net.anotheria.anoprise.cache;

public interface CacheFactory<K,V> {
	public Cache<K,V> create(String name, int startSize, int maxSize);
}

