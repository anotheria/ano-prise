package net.anotheria.anoprise.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import net.java.dev.moskito.core.predefined.CacheStats;
import net.java.dev.moskito.core.predefined.Constants;
import net.java.dev.moskito.core.producers.IStats;
import net.java.dev.moskito.core.producers.IStatsProducer;
import net.java.dev.moskito.core.registry.IProducerRegistry;
import net.java.dev.moskito.core.registry.ProducerRegistryFactory;

public abstract class AbstractCache implements IStatsProducer{

	
	
	
	private List<IStats> stats;
	private CacheStats cacheStats;

	private String name;
	private static final AtomicInteger instanceCounter = new AtomicInteger(0);

	
	protected AbstractCache(String aName){
		name = aName;
		
		stats = new ArrayList<IStats>();
		cacheStats = new CacheStats(name, Constants.DEFAULT_INTERVALS);
		stats.add(cacheStats);
		
		//This block of code is potentially unsafe, but since caches aren't created all the time but only at the start, 
		//we can live with it.
		IProducerRegistry reg = ProducerRegistryFactory.getProducerRegistryInstance();
		if (reg.getProducer(name)==null){
			reg.registerProducer(this);
		}else{
			String myName = null;
			do{
				myName = name+"-"+instanceCounter.incrementAndGet();
			}while(reg.getProducer(myName)!=null);
			name = myName;
			reg.registerProducer(this);
		}

	}
	
	protected static String getUnnamedInstanceName(Class<?> that){
		return that.getSimpleName()+"-"+instanceCounter.incrementAndGet();
	}
	
	@Override public String getCategory() {
		return "cache";
	}

	@Override public String getProducerId() {
		return getName();
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
