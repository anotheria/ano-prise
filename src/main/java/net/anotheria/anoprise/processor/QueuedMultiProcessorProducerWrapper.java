package net.anotheria.anoprise.processor;

import net.anotheria.moskito.core.logging.DefaultStatsLogger;
import net.anotheria.moskito.core.logging.IntervalStatsLogger;
import net.anotheria.moskito.core.logging.SL4JLogOutput;
import net.anotheria.moskito.core.producers.IStats;
import net.anotheria.moskito.core.producers.IStatsProducer;
import net.anotheria.moskito.core.stats.DefaultIntervals;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for QueuedMultiProcessor - moskito integration.
 *
 * @version $Id: $Id
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
	
	/**
	 * <p>Constructor for QueuedMultiProcessorProducerWrapper.</p>
	 *
	 * @param aCache a {@link net.anotheria.anoprise.processor.QueuedMultiProcessor} object.
	 * @param aProducerId a {@link java.lang.String} object.
	 * @param aCategory a {@link java.lang.String} object.
	 * @param aSubsystem a {@link java.lang.String} object.
	 */
	public QueuedMultiProcessorProducerWrapper(QueuedMultiProcessor<?> aCache, String aProducerId, String aCategory, String aSubsystem){
		producerId = aProducerId;
		category = aCategory;
		subsystem = aSubsystem;
		processor = aCache;
		stats = new ArrayList<IStats>();
		stats.add(processor.getQueueStat());
		stats.add(processor.getProcessorStats());
	}

	/** {@inheritDoc} */
	@Override
	public String getCategory() {
		return category;
	}

	/** {@inheritDoc} */
	@Override
	public String getProducerId() {
		return producerId;
	}

	/** {@inheritDoc} */
	@Override
	public List<IStats> getStats() {
		return stats;
	}

	/** {@inheritDoc} */
	@Override
	public String getSubsystem() {
		return subsystem;
	}
	
	/**
	 * <p>attachToMoskitoLoggers.</p>
	 */
	public void attachToMoskitoLoggers(){
		new DefaultStatsLogger(this, new SL4JLogOutput(LoggerFactory.getLogger("MoskitoDefault")));
		new IntervalStatsLogger(this, DefaultIntervals.FIVE_MINUTES, new SL4JLogOutput(LoggerFactory.getLogger("Moskito5m")));
		new IntervalStatsLogger(this, DefaultIntervals.FIFTEEN_MINUTES, new SL4JLogOutput(LoggerFactory.getLogger("Moskito15m")));
		new IntervalStatsLogger(this, DefaultIntervals.ONE_HOUR, new SL4JLogOutput(LoggerFactory.getLogger("Moskito1h")));
		new IntervalStatsLogger(this, DefaultIntervals.ONE_DAY, new SL4JLogOutput(LoggerFactory.getLogger("Moskito1d")));
		
	}

}
