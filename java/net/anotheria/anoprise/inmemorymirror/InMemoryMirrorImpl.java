package net.anotheria.anoprise.inmemorymirror;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class InMemoryMirrorImpl<K, V extends Mirrorable<K>> implements InMemoryMirror<K, V>{
	
	
	private InMemorySupport<K, V> support;
	private volatile Map<K,V> cache = null;
	private ReadWriteLock lock = new ReentrantReadWriteLock();

	public InMemoryMirrorImpl(String configName, InMemorySupport<K, V> aSupport) {
		support = aSupport;
	}
	
	@Override
	public Collection<V> getAll() throws InMemoryMirrorException{
		return getCache().values();
	}
	
	private Map<K, V> getCache() throws InMemoryMirrorException{
		try{
			lock.readLock().lock();
			if (cache!=null)
				return cache;
		}finally{
			lock.readLock().unlock();
		}

		try{
			lock.writeLock().lock();
			if (cache!=null)
				return cache;
			
			Collection<V> fromSupport = support.readAll();
			HashMap<K, V> map = new HashMap<K, V>(fromSupport.size());
			for (V v : fromSupport)
				map.put(v.getKey(), v);
			cache = map;
			return cache;
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	@Override
	public V get(K key) throws InMemoryMirrorException{
		V ret = getCache().get(key);
		if (ret==null)
			throw new ElementNotFoundException(key.toString());
		return ret;
	}

	@Override
	public V remove(K key) throws InMemoryMirrorException{
		try{
			lock.writeLock().lock();
			V fromSupport = support.remove(key);
			//technically next call is not necessary if fromSupport == null, but in order to reduce possible inconsistency we still do it.
			getCache().remove(key);
			return fromSupport;
		}finally{
			lock.writeLock().unlock();
		}
		
	}

	@Override
	public void update(V element) throws InMemoryMirrorException{
		try{
			lock.writeLock().lock();
			support.update(element);
			getCache().put(element.getKey(), element);
		}finally{
			lock.writeLock().unlock();
		}
	}

	@Override
	public V create(V element) throws InMemoryMirrorException {
		try{
			lock.writeLock().lock();
			V created = support.create(element);
			getCache().put(created.getKey(), created);
			return created;
		}catch(Exception exception){
			throw new InMemoryMirrorException("create(" + element + ")", exception);
		}finally{
			lock.writeLock().unlock();
		}
	}
	

}
