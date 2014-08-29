package net.anotheria.anoprise.cache;

import net.anotheria.moskito.core.predefined.CacheStats;

import java.util.HashMap;

public class RequestBasedCache<K,V> extends AbstractCache implements Cache<K, V>{
	
	/**
	 * A copy of the CacheStats for optimized access.
	 */
	private CacheStats cacheStatsCopy = null;

	public RequestBasedCache(String name){
		super(name);
		
		cacheStatsCopy = getCacheStats();
	}
	
	public RequestBasedCache(){
		this(getUnnamedInstanceName(RequestBasedCache.class));
	}
	
	
	@Override
	public V get(K id) {
		cacheStatsCopy.addRequest();
		V ret = (V)mapHolder.get().get(id);
		if (ret!=null)
			cacheStatsCopy.addHit();
		return ret;
	}

	@Override
	public void put(K id, V cacheable) {
		cacheStatsCopy.addWrite();
		mapHolder.get().put(id, cacheable);
		
	}

	@Override
	public void remove(K id) {
		cacheStatsCopy.addDelete();
		mapHolder.get().remove(id);
		
	}

	@Override
	public void clear() {
		mapHolder.get().clear();
	}

	@Override public String toString(){
		return super.toString();
	}
	
	
	/**
	 * The thread local variable associated with the current thread.
	 */
	private static InheritableThreadLocal<HashMap> mapHolder = new InheritableThreadLocal<HashMap>() {
		@Override
		protected synchronized HashMap initialValue() {
			return new HashMap();
		}

		@Override
		protected HashMap childValue(HashMap parentValue) {
			HashMap ret = new HashMap();
			ret.putAll(parentValue);
			return ret;
		}
	};
	
	
}
