package net.anotheria.anoprise.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

import net.java.dev.moskito.core.predefined.CacheStats;


/**
 * A simple RoundRobin implementation of the cache. This implementation stores the data in an array and uses two hashmaps for index2id and id2index resolution. All accesses to the cache are synchronized. The cache is threadsafe.
 * @author lrosenberg
 */
public class BoundedHardwiredCache<K,V> extends AbstractCache implements BoundedCache<K,V> {
	
	/**
	 * Default max size.
	 */
	public static final int DEF_MAX_SIZE   = 3000;
	
	/**
	 * Internal storage.
	 */
	private ConcurrentHashMap<K, V> cache;
	/**
	 * Lock to ensure bounds. 
	 */
	private Semaphore lock;
	/**
	 * Configured maximum size.
	 */
	private int maxSize;
	/**
	 * A copy of the CacheStats for optimized access.
	 */
	private CacheStats cacheStatsCopy = null;
	
	/**
	 * Creates a new cache.
	 */
	public BoundedHardwiredCache(){
		this(getUnnamedInstanceName(BoundedHardwiredCache.class));
	}
	/**
	 * Creates a new cache with given start and max size.
	 * @param aStartSize the start size.
	 * @param aMaxSize the max size.
	 */
	public BoundedHardwiredCache(int aMaxSize){
		this(getUnnamedInstanceName(BoundedHardwiredCache.class), aMaxSize);
	}
	
	/**
	 * Creates a new named cache with default start and max size and increment.
	 * @param name
	 */
	public BoundedHardwiredCache(String name){
		this(name, DEF_MAX_SIZE);
	}
	/**
	 * Creates a new named cache with given start and max size.
	 * @param name
	 * @param aStartSize
	 * @param aMaxSize
	 */
	public BoundedHardwiredCache(String name, int aMaxSize){
		super(name);
		
		
		maxSize   = aMaxSize;
		cacheStatsCopy = getCacheStats();
		init();		
	}
	
	@Override public void remove(K id){
		V old = cache.remove(id);
		if (old!=null)
			lock.release();
	}
	
	
	@Override public V get(K id){
		cacheStatsCopy.addRequest();
		V ret = cache.get(id);
		if (ret!=null)
			cacheStatsCopy.addHit();
		return ret;
	}
	
	
	@Override public boolean offer(K id, V cacheable){

		cacheStatsCopy.addWrite();
		if (!lock.tryAcquire()){
			cacheStatsCopy.addCacheFull();
			return false;
		}
		
		V old = cache.put(id, cacheable);
		if (old!=null)
			lock.release();
		return true;
	}
	
	private void init(){
		clear();
	}
	
	@Override public synchronized void clear(){
		cache = new ConcurrentHashMap<K, V>(maxSize);
		lock = new Semaphore(maxSize);
	}
	
	@Override public String toString(){
		if (cache==null)
			return getName()+" - not initialized.";
		String ret = getName()+" ";
		ret += " MaxSize: "+maxSize+", remaining elements: "+lock.availablePermits()+", realSize: "+cache.size();
		return ret;
	}
}
