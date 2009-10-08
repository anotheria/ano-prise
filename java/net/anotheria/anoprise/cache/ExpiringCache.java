/* ------------------------------------------------------------------------- *
$Source$
$Author$
$Date$
$Revision$


Copyright 2004-2005 by FriendScout24 GmbH, Munich, Germany.
All rights reserved.

This software is the confidential and proprietary information
of FriendScout24 GmbH. ("Confidential Information").  You
shall not disclose such Confidential Information and shall use
it only in accordance with the terms of the license agreement
you entered into with FriendScout24 GmbH.
See www.friendscout24.de for details.
** ------------------------------------------------------------------------- */
package net.anotheria.anoprise.cache;

import net.java.dev.moskito.core.predefined.CacheStats;


public class ExpiringCache<K,V> implements Cache<K,V>{


	private long expirationTime;
	
	
	private Cache<K, CachedObjectWrapper<V>> cache;
	private CacheStats moskitoCacheStats;
	
	public ExpiringCache(String name, int aStartSize, int aMaxSize, long anExpirationTime, CacheFactory<K, CachedObjectWrapper<V>> underlyingCacheFactory){
		cache = underlyingCacheFactory.create(name, aStartSize, aMaxSize);
		expirationTime = anExpirationTime;
		moskitoCacheStats = cache.getCacheStats();
	}

	
	
	public V get(K id) {
		CachedObjectWrapper<V> wrapper = cache.get(id);
		if (wrapper==null)
			return null;
		long now = System.currentTimeMillis();
		
		
		if (now-wrapper.getTimestamp()>expirationTime){
			moskitoCacheStats.addExpired();
			remove(id);
			return null;
		}
		
		return wrapper.getObj();
	}

	public void put(K id, V cacheable) {
		cache.put(id, new CachedObjectWrapper<V>(cacheable));
	}
	
	@Override public String toString(){
		return cache.toString()+", ExpirationTime: "+expirationTime;
	}
	
	public void clear() {
		cache.clear();
	}

	public void remove(K id) {
		cache.remove(id);
	}

	public CacheStats getCacheStats() {
		return moskitoCacheStats;
	}
}
