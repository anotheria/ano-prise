package net.anotheria.anoprise.processor;

import net.anotheria.anoprise.queue.BoundedFifoQueueFactory;
import net.anotheria.anoprise.queue.EnterpriseQueueFactory;
import org.slf4j.Logger;

import java.util.List;

/**
 *
 * Use this class to build new QueuedMultiProcessor. If some processor
 * parameters are not set during the building default values will be used.
 *
 * Can be build processors with two types of workers:
 * <ol>
 * <li>ElementWorker to perform work on a single element at one processing cycle
 * </li>
 * <li>PackageWorker to perform work on a package of element at one processing cycle
 * </li>
 *
 * @author dmetelin
 * @version $Id: $Id
 */
public class QueuedMultiProcessorBuilder<E>{

	/**
	 * Default time to sleep.
	 */
	public static final long DEF_SLEEP_TIME = 50;
	/**
	 * Default queue size.
	 */
	public static final int DEF_QUEUE_SIZE = 1000;

	/** Constant <code>DEF_QUEUE_FACTORY_CLASS</code> */
	public static final Class<?> DEF_QUEUE_FACTORY_CLASS = BoundedFifoQueueFactory.class;

	/** Constant <code>DEF_PROCESSOR_CHANNELS=10</code> */
	public static final int DEF_PROCESSOR_CHANNELS = 10;

	private long sleepTime;

	private int queueSize;

	private Class<EnterpriseQueueFactory<E>> queueFactoryClass;

	private int processorChannels;
	
	private boolean attachMoskitoLoggers = false;
	
	private String moskitoProducerId;
	
	private String moskitoCategory;
	
	private String moskitoSubsystem;
	
	private Logger processingLog;


	/**
	 * Creates new builder
	 */
	@SuppressWarnings("unchecked")
	public QueuedMultiProcessorBuilder() {
		queueSize = DEF_QUEUE_SIZE;
		processorChannels = DEF_PROCESSOR_CHANNELS;
		sleepTime = DEF_SLEEP_TIME;
		queueFactoryClass = (Class<EnterpriseQueueFactory<E>>) DEF_QUEUE_FACTORY_CLASS;
		processingLog = null;
	}

	/**
	 * Builds processor to perform work under single element at one processing
	 * cycle: single element is dequeued and sent to the element worker.
	 *
	 * @param name
	 *            (identifier) for this processor
	 * @param worker
	 *            for the package of elements
	 * @return processor
	 */
	public QueuedMultiProcessor<E> build(String name, ElementWorker<E> worker) {
		return build(name, new PackageWorkerAdapter<E>(worker));
	}

	/**
	 * Builds processor to perform work under elements package at one processing
	 * cycle: elements from the queue are gathered to the package and sent to
	 * the package worker
	 *
	 * @param name
	 *            (identifier) for this processor
	 * @param worker
	 *            for the package of elements
	 * @return processor
	 */
	public QueuedMultiProcessor<E> build(String name, PackageWorker<E> worker) {
		try {
			EnterpriseQueueFactory<E> queueFactory = queueFactoryClass.newInstance();
			QueuedMultiProcessor<E> ret = new QueuedMultiProcessor<E>(name, worker, queueFactory, queueSize, processorChannels, sleepTime, processingLog); 
			if(attachMoskitoLoggers)
				new QueuedMultiProcessorProducerWrapper(ret, moskitoProducerId, moskitoCategory, moskitoSubsystem).attachToMoskitoLoggers();
			return ret;
		} catch (Exception e) {
			throw new RuntimeException("Could not build QueuedMultiProcessor with name " + name + ": ", e);
		}
	}

	/**
	 * Sets the maximal size of the processor Queue
	 *
	 * @param queueSize a int.
	 * @return a {@link net.anotheria.anoprise.processor.QueuedMultiProcessorBuilder} object.
	 */
	public QueuedMultiProcessorBuilder<E> setQueueSize(int queueSize) {
		this.queueSize = queueSize;
		return this;
	}

	/**
	 * Sets factory to be used for creating Queue
	 *
	 * @param queueFactoryClass a {@link java.lang.Class} object.
	 * @return a {@link net.anotheria.anoprise.processor.QueuedMultiProcessorBuilder} object.
	 */
	public QueuedMultiProcessorBuilder<E> setQueueFactoryClass(Class<EnterpriseQueueFactory<E>> queueFactoryClass) {
		this.queueFactoryClass = queueFactoryClass;
		return this;
	}

	/**
	 * Sets number of async channels of processor in which work is performed.
	 *
	 * @param processorChannels a int.
	 * @return builder
	 */
	public QueuedMultiProcessorBuilder<E> setProcessorChannels(int processorChannels) {
		this.processorChannels = processorChannels;
		return this;
	}
	
	/**
	 * <p>Setter for the field <code>processingLog</code>.</p>
	 *
	 * @param processingLog a {@link org.slf4j.Logger} object.
	 * @return a {@link net.anotheria.anoprise.processor.QueuedMultiProcessorBuilder} object.
	 */
	public QueuedMultiProcessorBuilder<E>  setProcessingLog(Logger processingLog) {
		this.processingLog = processingLog;
		return this;
	}

	/**
	 * Sets sleep time to wait a new element arriving if queue is empty.
	 *
	 * @param sleepTime a long.
	 * @return builder
	 */
	public QueuedMultiProcessorBuilder<E> setSleepTime(long sleepTime) {
		this.sleepTime = sleepTime;
		return this;
	}
	
	/**
	 * <p>attachMoskitoLoggers.</p>
	 *
	 * @param producerId a {@link java.lang.String} object.
	 * @param category a {@link java.lang.String} object.
	 * @param subsystem a {@link java.lang.String} object.
	 * @return a {@link net.anotheria.anoprise.processor.QueuedMultiProcessorBuilder} object.
	 */
	public QueuedMultiProcessorBuilder<E> attachMoskitoLoggers(String producerId, String category, String subsystem){
		attachMoskitoLoggers = true;
		moskitoProducerId = producerId;
		moskitoCategory = category;
		moskitoSubsystem = subsystem;
		return this;
	}

	/**
	 * Adapts ElementWorker to PackageWorker for single element processing.
	 * 
	 * @author dmetelin
	 * 
	 * @param <T>
	 */
	private class PackageWorkerAdapter<T> implements PackageWorker<T> {

		private ElementWorker<T> elementWorker;

		public PackageWorkerAdapter(ElementWorker<T> anElementWorker) {
			elementWorker = anElementWorker;
		}

		@Override
		public void doWork(List<T> workingPackage) throws Exception {
			elementWorker.doWork(workingPackage.get(0));
		}

		@Override
		public int packageCapacity() {
			return 1;
		}

	}

}
