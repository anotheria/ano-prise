package net.anotheria.anoprise.processor;

import net.anotheria.moskito.core.logging.LoggerUtil;
import net.anotheria.moskito.core.producers.IStats;
import net.anotheria.moskito.core.producers.IStatsProducer;

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
	 * Attach ToMoskitoLoggers.
	 */
	public void attachToMoskitoLoggers(){
		LoggerUtil.createSLF4JDefaultAndIntervalStatsLogger(this);
	}

}
