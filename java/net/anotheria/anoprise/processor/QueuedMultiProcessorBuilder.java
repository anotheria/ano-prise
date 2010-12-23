package net.anotheria.anoprise.processor;

import java.util.List;

import net.anotheria.anoprise.queue.BoundedFifoQueueFactory;
import net.anotheria.anoprise.queue.EnterpriseQueueFactory;

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
 * 
 */
public class QueuedMultiProcessorBuilder {

	/**
	 * Default time to sleep.
	 */
	public static final long DEF_SLEEP_TIME = 50;
	/**
	 * Default queue size.
	 */
	public static final int DEF_QUEUE_SIZE = 1000;

	@SuppressWarnings("unchecked")
	public static final Class<? extends EnterpriseQueueFactory> DEF_QUEUE_FACTORY_CLASS = BoundedFifoQueueFactory.class;

	public static final int DEF_PROCESSOR_CHANNELS = 10;

	private int queueSize;

	@SuppressWarnings("unchecked")
	private Class<EnterpriseQueueFactory> queueFactoryClass;

	private int processorChannels;

	private long sleepTime;

	/**
	 * Creates new builder
	 */
	@SuppressWarnings("unchecked")
	public QueuedMultiProcessorBuilder() {
		queueSize = DEF_QUEUE_SIZE;
		processorChannels = DEF_PROCESSOR_CHANNELS;
		sleepTime = DEF_SLEEP_TIME;
		queueFactoryClass = (Class<EnterpriseQueueFactory>) DEF_QUEUE_FACTORY_CLASS;
	}

	/**
	 * Builds processor to perform work under single element at one processing
	 * cycle: single element is dequeued and sent to the element worker.
	 * 
	 * @param <T>
	 * @param name
	 *            (identifier) for this processor
	 * @param worker
	 *            for the package of elements
	 * @return processor
	 */
	public <T> QueuedMultiProcessor<T> build(String name, ElementWorker<T> worker) {
		return build(name, new PackageWorkerAdapter<T>(worker));
	}

	/**
	 * Builds processor to perform work under elements package at one processing
	 * cycle: elements from the queue are gathered to the package and sent to
	 * the package worker
	 * 
	 * @param <T>
	 * @param name
	 *            (identifier) for this processor
	 * @param worker
	 *            for the package of elements
	 * @return processor
	 */
	public <T> QueuedMultiProcessor<T> build(String name, PackageWorker<T> worker) {
		try {
			@SuppressWarnings("unchecked")
			EnterpriseQueueFactory<T> queueFactory = queueFactoryClass.newInstance();
			return new QueuedMultiProcessor<T>(name, worker, queueFactory, queueSize, processorChannels, sleepTime);
		} catch (Exception e) {
			throw new RuntimeException("Could not build QueuedMultiProcessor with name " + name + ": ", e);
		}
	}

	/**
	 * Sets the maximal size of the processor Queue
	 * 
	 * @param queueSize
	 * @return
	 */
	public QueuedMultiProcessorBuilder setQueueSize(int queueSize) {
		this.queueSize = queueSize;
		return this;
	}

	/**
	 * Sets factory to be used for creating Queue
	 * 
	 * @param queueFactoryClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public QueuedMultiProcessorBuilder setQueueFactoryClass(Class<EnterpriseQueueFactory> queueFactoryClass) {
		this.queueFactoryClass = queueFactoryClass;
		return this;
	}

	/**
	 * Sets number of async channels of processor in which work is performed.
	 * 
	 * @param processorChannels
	 * @return builder
	 */
	public QueuedMultiProcessorBuilder setProcessorChannels(int processorChannels) {
		this.processorChannels = processorChannels;
		return this;
	}

	/**
	 * Sets sleep time to wait a new element arriving if queue is empty.
	 * 
	 * @param sleepTime
	 * @return builder
	 */
	public QueuedMultiProcessorBuilder setSleepTime(long sleepTime) {
		this.sleepTime = sleepTime;
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
