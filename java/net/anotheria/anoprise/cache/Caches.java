package net.anotheria.anoprise.cache;

/**
 * Utility for cache creation.
 * @author another
 *
 */
public final class Caches {

	
	
	public static final <K,V> Cache<K,V> createSoftReferenceCache(String name){
		return new RoundRobinSoftReferenceCache<K, V>(name);
	}
	
	public static final <K,V> Cache<K,V> createSoftReferenceCache(String name, int startSize, int maxSize){
		return new RoundRobinSoftReferenceCache<K, V>(name, startSize, maxSize);
	}
	
	public static final <K,V> Cache<K,V> createHardwiredCache(String name){
		return new RoundRobinHardwiredCache<K, V>(name);
	}
	
	public static final <K,V> Cache<K,V> createHardwiredCache(String name, int startSize, int maxSize){
		return new RoundRobinHardwiredCache<K, V>(name, startSize, maxSize);
	}
	
	public static final <K,V> Cache<K,V> createHardwiredExpiringCache(String name, int startSize, int maxSize, int expirationTime){
		Cache<K,CachedObjectWrapper<V>> underlyingCache = createHardwiredCache(name, startSize, maxSize);
		return new ExpiringCache<K, V>(name, expirationTime, underlyingCache);
	}
	
	public static final <K,V> Cache<K,V> createSoftReferenceExpiringCache(String name, int startSize, int maxSize, int expirationTime){
		Cache<K,CachedObjectWrapper<V>> underlyingCache = createSoftReferenceCache(name, startSize, maxSize);
		return new ExpiringCache<K, V>(name, expirationTime, underlyingCache);
	}
	
	
	
	
	
	
	
	
	
	
	private Caches(){
		//protect from instantiation.
	}
	
/*	public static enum Strategy{
		ROUNDROBIN,
		EXPIRATION
	}*/
}
