package net.anotheria.anoprise.cache;

import net.anotheria.moskito.core.predefined.CacheStats;

import java.lang.ref.SoftReference;
import java.util.HashMap;


/**
 * A simple RoundRobin implementation of the cache.
 * @author lrosenberg
 */
public class RoundRobinSoftReferenceExperimentalCache<K,V> extends AbstractCache implements Cache<K,V> {
	
	public static final int DEF_START_SIZE = 1000;
	public static final int DEF_MAX_SIZE   = 3000;
	public static final float DEF_INCREMENT = 0.5F; 
	
	private SoftReference<V>[] cache;
	
	private HashMap<K,Integer> id2index;
	private HashMap<Integer,K> index2id;
	
	private int maxSize, startSize;
	private float increment;
	private int currentSize;
	
	private int lastElement ;
	private boolean firstCycleComplete;
	private boolean enlargeable;
	
	private CacheStats cacheStatsCopy = null;
	
	
	public RoundRobinSoftReferenceExperimentalCache(){
		this(getUnnamedInstanceName(RoundRobinSoftReferenceExperimentalCache.class));
	}
	
	public RoundRobinSoftReferenceExperimentalCache(int aStartSize, int aMaxSize){
		this(getUnnamedInstanceName(RoundRobinSoftReferenceExperimentalCache.class), aStartSize, aMaxSize);
	}
	
	
	public RoundRobinSoftReferenceExperimentalCache(int aStartSize, int aMaxSize, float anIncrement){
		this(getUnnamedInstanceName(RoundRobinSoftReferenceExperimentalCache.class), aStartSize, aMaxSize, anIncrement);
	}
	
	public RoundRobinSoftReferenceExperimentalCache(String name){
		this(name, DEF_START_SIZE, DEF_MAX_SIZE, DEF_INCREMENT);
	}
	
	public RoundRobinSoftReferenceExperimentalCache(String name, int aStartSize, int aMaxSize){
		this(name, aStartSize, aMaxSize, DEF_INCREMENT);
	}
	
	
	public RoundRobinSoftReferenceExperimentalCache(String name, int aStartSize, int aMaxSize, float anIncrement){
		super(name);
		this.startSize = aStartSize;
		this.maxSize   = aMaxSize;
		this.increment = anIncrement;
		
		cacheStatsCopy = getCacheStats();
		
		init();		

	}
	
	public synchronized void remove(K id){
		cacheStatsCopy.addDelete();
		Integer index = getCachePosition(id);
		
		if (index==null)
			return ;
			
		id2index.remove(id);
		index2id.remove(index);
		
		SoftReference<V> ref = cache[index.intValue()];
		cache[index.intValue()] = null;

		if (ref.get()==null){
			cacheStatsCopy.addGarbageCollected();
		}else{
			ref.clear();
		}


	}
	
	
	@Override public synchronized V get(K id){
		cacheStatsCopy.addRequest();
		//System.out.println("Requested id: ."+id+".");
		Integer index = getCachePosition(id);
		//System.out.println("Index for id: "+index);
		if (index==null)
			return null;
		V toRet = cache[index.intValue()].get();

		if (toRet==null){
			cacheStatsCopy.addGarbageCollected();
			id2index.remove(id);
			index2id.remove(index);
		}else{
			cacheStatsCopy.addHit();
		}

		return toRet;  
	}
	
	private Integer getCachePosition(K id){
		return id2index.get(id);
	}
	
	@SuppressWarnings("unchecked")
	@Override public synchronized void put(K id, V cacheable){
		//System.out.println("--> put in cache "+id+", "+cacheable);
		
		SoftReference<V> toPut = new SoftReference<V>(cacheable);
		cacheStatsCopy.addWrite();
		
		Integer oldPosition = getCachePosition(id);
		if (oldPosition!=null){
			//System.out.println("replacing old entry.");
			cache[oldPosition.intValue()] = toPut;
			return;
		}
		
		if (lastElement < currentSize-1 && !firstCycleComplete){
			//es ist noch platz im array.
			lastElement++;
			Integer tmpIndex = Integer.valueOf(lastElement);
			index2id.put(tmpIndex, id);
			id2index.put(id, tmpIndex);
			cache[lastElement] = toPut;
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
			cache[lastElement] = toPut;
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
				
				SoftReference<V>[] oldCache = cache;
				cache = new SoftReference[newsize];
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
		cache = new SoftReference[startSize];
		id2index  = new HashMap<K,Integer>(startSize);
		index2id  = new HashMap<Integer,K>(startSize);

		lastElement = -1;
		firstCycleComplete = false;	
		enlargeable = startSize < maxSize;
		currentSize = startSize;
	}
	
	@Override public String toString(){
		if (cache==null)
			return "Not initialized cache "+getName();
		String ret = getName()+" ";
		ret += "CurrentSize: "+cache.length+", MaxSize: "+maxSize;
		ret += " Enlargement possible: "+enlargeable+", FirstCycleComplete: "+firstCycleComplete;
		ret += " lastElement: "+lastElement;
		return ret;
	}
}
