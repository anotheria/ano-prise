package net.anotheria.anoprise.cache;


public class CacheControllerWithExpiry<K,V> extends CacheController<K,V>{
	
	private long expirationTime;
	
	private CacheFactory<K, CachedObjectWrapper<V>> factory;

	public CacheControllerWithExpiry(String configurationName, CacheFactory<K, CachedObjectWrapper<V>> aFactory){
		super(configurationName);
		factory = aFactory;
	}

	@Override
	protected Cache<K,V> createCache(int startSize, int maxSize) {
		return  new ExpiringCache<K,V>(getConfigurationName(), startSize, maxSize, expirationTime, factory);
	}

	public void setExpirationTime(long anExpirationTime){
		expirationTime = anExpirationTime;
		if (getCache()!=null)
			((ExpiringCache<K,V>)getCache()).setExpirationTime(expirationTime);
	}
	
	
}
