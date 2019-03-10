package net.anotheria.anoprise.cache;

import net.anotheria.moskito.core.predefined.CacheStats;

import java.util.HashMap;


/**
 * A simple RoundRobin implementation of the cache. This implementation stores the data in an array and uses two hashmaps for index2id and id2index resolution. All accesses to the cache are synchronized. The cache is threadsafe.
 * @author lrosenberg
 */
public class RoundRobinHardwiredCache<K,V> extends AbstractCache implements Cache<K,V> {
	
	
	/**
	 * Default start size of the internal array.
	 */
	public static final int DEF_START_SIZE = 1000;
	/**
	 * Default max size of the internal array.
	 */
	public static final int DEF_MAX_SIZE   = 3000;
	/**
	 * Default array size increment.
	 */
	public static final float DEF_INCREMENT = 0.5F; 
	
	/**
	 * Cache array. 
	 */
	private V[] cache;
	/**
	 * Map for id2index resolve.
	 */
	private HashMap<K,Integer> id2index;
	/**
	 * Map for index2id (reverse) resolve.
	 */
	private HashMap<Integer,K> index2id;
	/**
	 * Max size of the cache.
	 */
	private int maxSize;
	/**
	 * Start size of the cache.
	 */
	private int startSize;
	/**
	 * Increment factor for size increasement.
	 */
	private float increment;
	/**
	 * Current cache size.
	 */
	private int currentSize;
	/**
	 * Last written element.
	 */
	private int lastElement ;
	/**
	 * True if the first cycle is complete.
	 */
	private boolean firstCycleComplete;
	/**
	 * True if the cache is enlargeable.
	 */
	private boolean enlargeable;
	/**
	 * A copy of the CacheStats for optimized access.
	 */
	private CacheStats cacheStatsCopy = null;
	
	/**
	 * Creates a new cache.
	 */
	public RoundRobinHardwiredCache(){
		this(getUnnamedInstanceName(RoundRobinHardwiredCache.class));
	}
	/**
	 * Creates a new cache with given start and max size.
	 * @param aStartSize the start size.
	 * @param aMaxSize the max size.
	 */
	public RoundRobinHardwiredCache(int aStartSize, int aMaxSize){
		this(getUnnamedInstanceName(RoundRobinHardwiredCache.class), aStartSize, aMaxSize);
	}
	
	/**
	 * Creates a new cache with given start and max size, and size increment.
	 * @param aStartSize cache start size.
	 * @param aMaxSize cache max size.
	 * @param anIncrement increment percentage for size increases.
	 */
	public RoundRobinHardwiredCache(int aStartSize, int aMaxSize, float anIncrement){
		this(getUnnamedInstanceName(RoundRobinHardwiredCache.class), aStartSize, aMaxSize, anIncrement);
	}
	/**
	 * Creates a new named cache with default start and max size and increment.
	 * @param name
	 */
	public RoundRobinHardwiredCache(String name){
		this(name, DEF_START_SIZE, DEF_MAX_SIZE, DEF_INCREMENT);
	}
	/**
	 * Creates a new named cache with given start and max size.
	 * @param name name of the cache.
	 * @param aStartSize start cache size.
	 * @param aMaxSize max cache size.
	 */
	public RoundRobinHardwiredCache(String name, int aStartSize, int aMaxSize){
		this(name, aStartSize, aMaxSize, DEF_INCREMENT);
	}
	
	
	/**
	 * Creates a new RoundRobinHardwiredCache.
	 * @param name name of the cache.
	 * @param aStartSize start size of the cache.
	 * @param aMaxSize max size of the cache.
	 * @param anIncrement increment of the internal array.
	 */
	public RoundRobinHardwiredCache(String name, int aStartSize, int aMaxSize, float anIncrement){
		super(name);
		startSize = aStartSize;
		maxSize   = aMaxSize;
		increment = anIncrement;
		
		cacheStatsCopy = getCacheStats();
		
		init();		

	}
	
	@Override public synchronized void remove(K id){
		cacheStatsCopy.addDelete();
		Integer index = id2index.get(id);
		
		if (index==null)
			return ;
		
			
		id2index.remove(id);
		index2id.remove(index);
		
		//V v = cache[index.intValue()]; - in case we need to return something
		cache[index.intValue()] = null;
	}
	
	
	@Override public synchronized V get(K id){
		cacheStatsCopy.addRequest();
		//System.out.println("Requested id: ."+id+".");
		Integer index = id2index.get(id);
		//System.out.println("Index for id: "+index);
		if (index==null)
			return null;
		V toRet = cache[index.intValue()];

		if (toRet==null){
			//this can't happen!:-)
			cacheStatsCopy.addGarbageCollected();
			id2index.remove(id);
			index2id.remove(index);
		}else{
			cacheStatsCopy.addHit();
		}

		return toRet;  
	}
	
	
	@SuppressWarnings("unchecked")
	@Override public synchronized void put(K id, V cacheable){
		//System.out.println("--> put in cache "+id+", "+cacheable);
		
		cacheStatsCopy.addWrite();
		
		Integer oldPosition = id2index.get(id);
		if (oldPosition!=null){
			cache[oldPosition.intValue()] = cacheable;
			return;
		}
		
		if (lastElement < currentSize-1 && !firstCycleComplete){
			//es ist noch platz im array.
			lastElement++;
			Integer tmpIndex = Integer.valueOf(lastElement);
			index2id.put(tmpIndex, id);
			id2index.put(id, tmpIndex);
			cache[lastElement] = cacheable;
			return;
		}

		if (firstCycleComplete){
			//das bedeutet, dass wir schon voll sind, und rotieren muessen.
			lastElement++;
			if (lastElement==cache.length)
				lastElement = 0;
			Integer tmp = Integer.valueOf(lastElement);
			Object oldId = index2id.get(tmp);
			if (oldId!=null){
				id2index.remove(oldId);
			}
			id2index.put(id, tmp);
			index2id.put(tmp, id);
			cache[lastElement] = cacheable;
			cacheStatsCopy.addRollover();
			return; 					
		}
		
		//wenn wir hier angekommen sind, sind wir full.
		if (!firstCycleComplete){
			//also lastElement == maxSize ist gegeben, vergroessern
			if (enlargeable){
				float wishedSize = currentSize*(1+increment);
				//System.out.println("wished: "+wishedSize);
				int newsize = (int)wishedSize;
				if (newsize>maxSize)
					newsize = maxSize;
				
				//System.out.println("Enlarging from "+cache.length+" to "+newsize+".");	
				
				V[] oldCache = cache;
				cache = (V[])new Object[newsize];
				System.arraycopy(oldCache, 0, cache, 0, currentSize);
				currentSize = newsize;
				
				enlargeable = newsize < maxSize;
				
				//System.out.println("now enlargeable: "+enlargeable);
				firstCycleComplete = false;
				put(id, cacheable);
				return;
				
			}
			//wir sind voll und nicht vergroesserbar.
			firstCycleComplete = true;
			put(id, cacheable);
			return;
		}
		
		//hier sollten wir nicht ankommen!
		throw new AssertionError("You couldn't reach this point in code! ("+this+")");
	}
	
	private void init(){
		clear();
	}
	

	@SuppressWarnings("unchecked")
	@Override public synchronized void clear(){
		cache = (V[])new Object[startSize];
		id2index  = new HashMap<K,Integer>(startSize);
		index2id  = new HashMap<Integer,K>(startSize);

		lastElement = -1;
		firstCycleComplete = false;	
		enlargeable = startSize < maxSize;
		currentSize = startSize;
	}
	
	@Override public String toString(){
		if (cache==null)
			return getName()+" - not initialized.";
		String ret = getName()+" ";
		ret += "CurrentSize: "+cache.length+", MaxSize: "+maxSize;
		ret += " Enlargement possible: "+enlargeable+", FirstCycleComplete: "+firstCycleComplete;
		ret += " lastElement: "+lastElement;
		return ret;
	}
	
	 

	
    


}
