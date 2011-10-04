package net.anotheria.anoprise.inmemorymirror;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public class InMemoryMirrorImpl<K, V extends Mirrorable<K>> implements InMemoryMirror<K, V>{
	
	
	private InMemorySupport<K, V> support;
	private volatile Map<K,V> cache = null;
	private ReadWriteLock lock = new ReentrantReadWriteLock();

	public InMemoryMirrorImpl(String configName, InMemorySupport<K, V> aSupport) {
		support = aSupport;
	}
	
	@Override
	public Collection<V> getAll() {
		return getCache().values();
	}
	
	private Map<K, V> getCache(){
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
	public V get(K id) {
		return getCache().get(id);
	}

	@Override
	public V remove(K id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(V element) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public V create(V element) {
		try{
			lock.writeLock().lock();
			V created = support.create(element);
			cache.put(created.getKey(), created);
			return created;
		}catch(Exception exception){
			throw new AssertionError("FIXME");
		}finally{
			lock.writeLock().unlock();
		}
	}
	

}
