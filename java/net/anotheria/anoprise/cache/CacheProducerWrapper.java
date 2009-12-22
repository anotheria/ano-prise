package net.anotheria.anoprise.cache;

import java.util.ArrayList;
import java.util.List;

import net.java.dev.moskito.core.producers.IStats;
import net.java.dev.moskito.core.producers.IStatsProducer;

/**
 * Helper class for cache - moskito integration.
 * @author another
 *
 */
public class CacheProducerWrapper implements IStatsProducer{
	/**
	 * Wrapped cache.
	 */
	private Cache<?,?> cache;
	/**
	 * Producer Id.
	 */
	private String producerId;
	/**
	 * Producer category.
	 */
	private String category;
	/**
	 * Producer subsystems.
	 */
	private String subsystem;
	/**
	 * Cached stats list.
	 */
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
