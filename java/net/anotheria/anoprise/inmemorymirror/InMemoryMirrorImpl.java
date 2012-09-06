package net.anotheria.anoprise.inmemorymirror;

import java.util.ArrayList;
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
		return new ArrayList<V>(getCache().values());
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
		return remove(key, false);
	}

	@Override
	public V removeLocalOnly(K id) throws InMemoryMirrorException {
		return remove(id, true);
	}

	protected V remove(K key, boolean localOnly) throws InMemoryMirrorException{
		try {
			lock.writeLock().lock();
			if (!localOnly) {
				support.remove(key);
			}
			return getCache().remove(key);
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public void update(V element) throws InMemoryMirrorException{
		update(element, false);
	}

	@Override
	public void updateLocalOnly(V element) throws InMemoryMirrorException, ElementNotFoundException {
		update(element, true);
	}

	protected void update(V element, boolean localOnly) throws InMemoryMirrorException{
		try{
			lock.writeLock().lock();
			if(!localOnly) {
				support.update(element);
			}
			getCache().put(element.getKey(), element);
		}finally{
			lock.writeLock().unlock();
		}
	}

	@Override
	public V create(V element) throws InMemoryMirrorException {
		return create(element, false);
	}

	@Override
	public V createLocalOnly(V element) throws InMemoryMirrorException {
		return create(element, true);
	}

	protected V create(V element, boolean localOnly) throws InMemoryMirrorException {
		try{
			lock.writeLock().lock();
			V created = element;
			if(!localOnly) {
				created = support.create(element);
			}
			getCache().put(created.getKey(), created);
			return created;
		}catch(Exception exception){
			throw new InMemoryMirrorException("create(" + element + ")", exception);
		}finally{
			lock.writeLock().unlock();
		}
	}
}
