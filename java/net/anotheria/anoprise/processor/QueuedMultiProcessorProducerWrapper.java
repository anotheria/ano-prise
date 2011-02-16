package net.anotheria.anoprise.processor;

import java.util.ArrayList;
import java.util.List;

import net.java.dev.moskito.core.logging.DefaultStatsLogger;
import net.java.dev.moskito.core.logging.IntervalStatsLogger;
import net.java.dev.moskito.core.logging.Log4JOutput;
import net.java.dev.moskito.core.producers.IStats;
import net.java.dev.moskito.core.producers.IStatsProducer;
import net.java.dev.moskito.core.stats.DefaultIntervals;

import org.apache.log4j.Logger;

/**
 * Helper class for QueuedMultiProcessor - moskito integration.
 * @author another, dmetelin
 *
 */
public class QueuedMultiProcessorProducerWrapper implements IStatsProducer{
	/**
	 * Wrapped QueuedMultiProcessor.
	 */
	private QueuedMultiProcessor<?> processor;
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
	
	public QueuedMultiProcessorProducerWrapper(QueuedMultiProcessor<?> aCache, String aProducerId, String aCategory, String aSubsystem){
		producerId = aProducerId;
		category = aCategory;
		subsystem = aSubsystem;
		processor = aCache;
		stats = new ArrayList<IStats>();
		stats.add(processor.getQueueStat());
		stats.add(processor.getProcessorStats());
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
	
	public void attachToMoskitoLoggers(){
		new DefaultStatsLogger(this, new Log4JOutput(Logger.getLogger("MoskitoDefault")));
		new IntervalStatsLogger(this, DefaultIntervals.FIVE_MINUTES, new Log4JOutput(Logger.getLogger("Moskito5m")));
		new IntervalStatsLogger(this, DefaultIntervals.FIFTEEN_MINUTES, new Log4JOutput(Logger.getLogger("Moskito15m")));
		new IntervalStatsLogger(this, DefaultIntervals.ONE_HOUR, new Log4JOutput(Logger.getLogger("Moskito1h")));
		new IntervalStatsLogger(this, DefaultIntervals.ONE_DAY, new Log4JOutput(Logger.getLogger("Moskito1d")));
		
	}

}
