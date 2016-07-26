package net.anotheria.anoprise.processor;

import net.anotheria.moskito.core.logging.DefaultStatsLogger;
import net.anotheria.moskito.core.logging.IntervalStatsLogger;
import net.anotheria.moskito.core.logging.SLF4JLogOutput;
import net.anotheria.moskito.core.producers.IStats;
import net.anotheria.moskito.core.producers.IStatsProducer;
import net.anotheria.moskito.core.stats.DefaultIntervals;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

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
		stats = new ArrayList<>();
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
		new DefaultStatsLogger(this, new SLF4JLogOutput(LoggerFactory.getLogger("MoskitoDefault")));
		new IntervalStatsLogger(this, DefaultIntervals.FIVE_MINUTES, new SLF4JLogOutput(LoggerFactory.getLogger("Moskito5m")));
		new IntervalStatsLogger(this, DefaultIntervals.FIFTEEN_MINUTES, new SLF4JLogOutput(LoggerFactory.getLogger("Moskito15m")));
		new IntervalStatsLogger(this, DefaultIntervals.ONE_HOUR, new SLF4JLogOutput(LoggerFactory.getLogger("Moskito1h")));
		new IntervalStatsLogger(this, DefaultIntervals.ONE_DAY, new SLF4JLogOutput(LoggerFactory.getLogger("Moskito1d")));
		
	}

}
