package net.anotheria.anoprise.cache;

import net.anotheria.moskito.core.predefined.CacheStats;
import net.anotheria.moskito.core.predefined.Constants;
import net.anotheria.moskito.core.producers.IStats;
import net.anotheria.moskito.core.producers.IStatsProducer;
import net.anotheria.moskito.core.registry.IProducerRegistry;
import net.anotheria.moskito.core.registry.ProducerRegistryFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Base class for cache implementations. Implements support for moskito monitoring in caches.
 * @author lrosenberg
 */
public abstract class AbstractCache implements IStatsProducer{

	/**
	 * List of stats as required by the IStatsProducer interface.
	 */
	private List<IStats> stats;
	/**
	 * CacheStats.
	 */
	private CacheStats cacheStats;

	/**
	 * Name of the cache.
	 */
	private String name;
	/**
	 * Instance counter for cache instances.
	 */
	private static final AtomicInteger instanceCounter = new AtomicInteger(0);

	/**
	 * Creates new AbstractCache and registers it as producer.
	 * @param aName
	 */
	protected AbstractCache(String aName){
		name = aName;
		
		stats = new ArrayList<>();
		cacheStats = new CacheStats(name, Constants.getDefaultIntervals());
		stats.add(cacheStats);
		
		//This block of code is potentially unsafe, but since caches aren't created all the time but only at the start, 
		//we can live with it.
		IProducerRegistry reg = ProducerRegistryFactory.getProducerRegistryInstance();
		if (reg.getProducer(name)==null){
			reg.registerProducer(this);
		}else{
			String myName = null;
			do{
				myName = name+ '-' +instanceCounter.incrementAndGet();
			}while(reg.getProducer(myName)!=null);
			name = myName;
			reg.registerProducer(this);
		}

	}
	
	protected static String getUnnamedInstanceName(Class<?> that){
		return that.getSimpleName()+ '-' +instanceCounter.incrementAndGet();
	}
	
	@Override public String getCategory() {
		return "cache";
	}

	@Override public String getProducerId() {
		return name;
	}

	@Override public List<IStats> getStats() {
		return stats;
	}

	@Override public String getSubsystem() {
		return "default";
	}
	
	public CacheStats getCacheStats(){
		return cacheStats;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
