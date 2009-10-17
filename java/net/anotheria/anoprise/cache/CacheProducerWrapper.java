package net.anotheria.anoprise.cache;

import java.util.ArrayList;
import java.util.List;

import net.java.dev.moskito.core.producers.IStats;
import net.java.dev.moskito.core.producers.IStatsProducer;

public class CacheProducerWrapper implements IStatsProducer{
	
	private Cache<?,?> cache;
	private String producerId;
	private String category;
	private String subsystem;
	private List<IStats> stats;
	
	public CacheProducerWrapper(Cache<?,?> aCache, String aProducerId, String aCategory, String aSubsystem){
		producerId = aProducerId;
		category = aCategory;
		subsystem = aSubsystem;
		cache = aCache;
		stats = new ArrayList<IStats>();
		stats.add(cache.getCacheStats());
	}

	@Override
	public String getCategory() {
		return category;
	}

	@Override
	public String getProducerId() {
		return producerId;
	}

	@Override
	public List<IStats> getStats() {
		return stats;
	}

	@Override
	public String getSubsystem() {
		return subsystem;
	}

}
