package net.anotheria.anoprise.processor;

import net.anotheria.anoprise.queue.BoundedFifoQueueFactory;
import net.anotheria.anoprise.queue.EnterpriseQueue;
import net.anotheria.anoprise.queue.EnterpriseQueueFactory;
import net.anotheria.moskito.core.predefined.QueueStats;
import net.anotheria.moskito.core.predefined.QueuingSystemStats;
import net.anotheria.util.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Processor to perform same type work under elements that arrives in non
 * deterministic time intervals. Each new element added the queue where it will
 * wait until processor async channel is available to perform the work.
 *
 * This is the implementation of QueueingSystem model.
 *
 * @author dmetelin
 * @param <T>
 * @version $Id: $Id
 */
public class QueuedMultiProcessor<T extends Object> extends Thread {

	/**
	 * The log for this processor.
	 */
	private Logger log;
	
	/**
	 * The factory for creating queues.
	 */
	private EnterpriseQueueFactory<T> queueFactory;

	/**
	 * Size of the queue.
	 */
	private final int queueSize;

	/**
	 * The queue in which items are stored.
	 */
	private EnterpriseQueue<T> queue;

	private MultiProcessor<T> processor;

	/**
	 * Time to sleep after an overflow happened and before retrying.
	 */
	private final long sleepTime;

	private QueuingSystemStats stats;

	private AtomicBoolean stopQueueing;

	private AtomicBoolean stopImmediately;

	private int packageCapacity;

	/**
	 * Creates a new QueuedProcessor. This is the standard constructor used by
	 * all other constructors.
	 *
	 * NOTE: instead of direct call of this constructor it is recommended to use
	 * QueuedMultiProcessorBuilder to create new QueuedProcessor
	 *
	 * @param aName
	 *            name of the processor.
	 * @param aWorker
	 *            worker for the queued processor.
	 * @param aQueueFactory
	 *            factory to create the underlying queue.
	 * @param aQueueSize
	 *            size of the queue.
	 * @param aSleepTime
	 *            sleep time in case of an overflow.
	 * @param aLog
	 *            logger for output. If null default will be used.
	 * @param aProcessingChannels a int.
	 */
	public QueuedMultiProcessor(String aName, PackageWorker<T> aWorker, EnterpriseQueueFactory<T> aQueueFactory, int aQueueSize, int aProcessingChannels, long aSleepTime, Logger aLog) {
		super(aName);
		setDaemon(true);

		log = aLog != null? aLog: LoggerFactory.getLogger(QueuedMultiProcessor.class);
		
		stats = new QueuingSystemStats(aName);
		stats.setServersSize(aProcessingChannels);
		stats.setQueueSize(aQueueSize);

		queueSize = aQueueSize;
		sleepTime = aSleepTime;

		queueFactory = aQueueFactory == null ? new BoundedFifoQueueFactory<T>() : aQueueFactory;

		packageCapacity = aWorker.packageCapacity();

		processor = new MultiProcessor<T>(aProcessingChannels, aWorker, aLog);
		processor.addListener(new WorkProcessingListener<T>() {
			@Override
			public void workStarted(List<T> workingPackage) {
			}

			@Override
			public void workFinished(List<T> workingPackage, long workDuration) {
				stats.addServicingTime(workDuration);
				stats.addServiced();
			}

			@Override
			public void workInterrupted(List<T> workingPackage) {
				stats.addError();
			}

		});

		init();
	}

	/**
	 * Initializes internal processor structurues.
	 */
	private void init() {
		queue = queueFactory.createQueue(queueSize);

		stopQueueing = new AtomicBoolean(false);
		stopImmediately = new AtomicBoolean(false);
	}

	/**
	 * Resets the queue. Useful for inittesting. Doesn't restart the processor
	 * but deletes the underlying queue. The elements stuck in queue will NOT be
	 * delivered.
	 */
	public void reset() {
		init();
	}

	/**
	 * Default method to add an element to the queue. Calls addToQueueDontWait
	 * internally.
	 *
	 * @param aElement a T object.
	 * @throws net.anotheria.anoprise.processor.UnrecoverableQueueOverflowException
	 *             if the processing queue is full.
	 */
	public void addToQueue(T aElement) throws UnrecoverableQueueOverflowException {
		addToQueueDontWait(aElement);
	}

	/**
	 * Inserts the specified element at the tail of the processing queue,
	 * waiting if necessary for space in the queue to become available
	 *
	 * @param element
	 *            the element to add
	 * @throws net.anotheria.anoprise.processor.UnrecoverableQueueOverflowException if any.
	 */
	public void addToQueueAndWait(T element) throws UnrecoverableQueueOverflowException {
		addToQueueAndWait(element, 0);
	}

	/**
	 * Inserts the specified element at the tail of the processing queue,
	 * waiting if necessary for space in the queue to become available
	 *
	 * @param element
	 *            the element to add
	 * @param timeout
	 *            the maximum time to wait in milliseconds.
	 * @throws net.anotheria.anoprise.processor.UnrecoverableQueueOverflowException if any.
	 */
	public void addToQueueAndWait(T element, long timeout) throws UnrecoverableQueueOverflowException {
		stats.addArrived();

		if (stopQueueing.get()) {
			stats.addThrowedAway();
			log.error(getName() + ": queueing is stopped! Throwing away " + element + ", " + getStatsString());
			throw new UnrecoverableQueueOverflowException(getName() + ": queueing is stopped! Throwing away " + element + ", " + getStatsString());
		}

		long startTime = System.nanoTime();
		while (timeout <= 0 || System.nanoTime() - startTime < timeout) {
			if (queue.offer(element))
				return;
			synchronized (queue) {
				stats.addWaited();
				long waitStart = System.nanoTime();
				ThreadUtils.waitIgnoreException(queue);
				long dur = System.nanoTime() - waitStart;
				stats.addWaitingTime(dur);
			}
		}
		stats.addThrowedAway();
		log.error("Waiting for enqueue timeout. Throwing away " + element + ", " + getStatsString());
		throw new UnrecoverableQueueOverflowException("Waiting for enqueue timeout. Throwing away : " + element + ", " + getStatsString());
	}

	/**
	 * Inserts the specified element at the tail of the processing queue if the
	 * queue is not full
	 *
	 * @param element a T object.
	 * @throws net.anotheria.anoprise.processor.UnrecoverableQueueOverflowException
	 *             if the processing queue is full.
	 */
	public void addToQueueDontWait(T element) throws UnrecoverableQueueOverflowException {
		addToQueueDontWait(element, 2, 100);
	}

	/**
	 * Inserts the specified element at the tail of the processing queue if the
	 * queue is not full
	 *
	 * @param element a T object.
	 * @throws net.anotheria.anoprise.processor.UnrecoverableQueueOverflowException
	 *             if the processing queue is full.
	 * @param enqueueTries a int.
	 * @param triesDelay a int.
	 */
	public void addToQueueDontWait(T element, int enqueueTries, int triesDelay) throws UnrecoverableQueueOverflowException {
		stats.addArrived();

		if (stopQueueing.get()) {
			stats.addThrowedAway();
			log.error(getName() + ": queueing is stopped! Throwing away " + element + ", " + getStatsString());
			throw new UnrecoverableQueueOverflowException(getName() + ": queueing is stopped! Throwing away " + element + ", " + getStatsString());
		}

		for (int i = 0; i < enqueueTries; i++) {
			if (queue.offer(element))
				return;
			long waitStart = System.nanoTime();
			synchronized (this) {
				ThreadUtils.sleepIgnoreException(triesDelay);
			}
			long dur = System.nanoTime() - waitStart;
			stats.addWaited();
			stats.addWaitingTime(dur);
		}
		stats.addThrowedAway();
		log.error("couldn't recover from queue overflow, throwing away " + element + ", " + getStatsString());
		throw new UnrecoverableQueueOverflowException("Element: " + element + ", " + getStatsString());
	}

	/** {@inheritDoc} */
	@Override
	public void run() {

		final AtomicBoolean shutdown = new AtomicBoolean(false);

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				log.info("Shutting down processor!");
				stopQueueing();
				shutdown.set(true);
				synchronized (shutdown) {
					log.info("Wait while queue processing complete...");
					ThreadUtils.waitIgnoreException(shutdown);
				}

				log.info("Processor is shutted down!");
				log.info(getStatsString());
			}
		});
		try {
			while (!stopImmediately.get()) {
				List<T> elementsPackage = new ArrayList<T>();
				T element;
				while (elementsPackage.size() < packageCapacity && (element = queue.poll()) != null) {
					// System.out.println("Packaging: " +
					// elementsPackage.size());
					elementsPackage.add(element);
				}

				// System.out.println("1");
				synchronized (queue) {
					queue.notify();
				}
				// System.out.println("2");
				if (elementsPackage.size() > 0) {
					// System.out.println("3");
					processor.process(elementsPackage);
					continue;
				}
				// System.out.println("4");
				if (shutdown.get()) {
					processor.shutdown();
					if (processor.isFinished()) {
						log.info("Queue is empty and all works are done. Processing completed!");
						synchronized (shutdown) {
							shutdown.notify();
						}
						break;
					}
				}
				ThreadUtils.sleepIgnoreException(sleepTime);
			}
		} catch (Throwable ttt) {
			try {
				log.error("run ", ttt);
			} catch (Exception e) {
				System.out.println(QueuedMultiProcessor.class + " Can't log!!!");
				ttt.printStackTrace();
			}
		}
	}

	/**
	 * <p>drainQueue.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<T> drainQueue() {
		return queue.drain();
	}

	/**
	 * Sends signal to stop the QueueProcessor running Thread after working all
	 * elements in the queue.
	 */
	public void stopQueueing() {
		stopQueueing.set(true);
	}

	/**
	 * Sends signal to stop the QueueProcessor running Thread after working
	 * current element.
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<T> stopImmediately() {
		stopImmediately.set(true);
		return drainQueue();
	}

	/**
	 * <p>isStopped.</p>
	 *
	 * @return true if processing was stopped by calling
	 *         stopAfterQueueProcessing() or stopImmediately().
	 */
	public boolean isStopped() {
		return stopImmediately.get() || stopQueueing.get();
	}

	/**
	 * <p>getProcessorStats.</p>
	 *
	 * @return a {@link net.anotheria.moskito.core.predefined.QueuingSystemStats} object.
	 */
	public QueuingSystemStats getProcessorStats() {
		return stats;
	}

	/**
	 * <p>getQueueStat.</p>
	 *
	 * @return a {@link net.anotheria.moskito.core.predefined.QueueStats} object.
	 */
	public QueueStats getQueueStat() {
		return queue.getQueueStats();
	}

	/**
	 * <p>getStatsString.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getStatsString() {
		return getProcessorStats().toStatsString() + ",\nQUEUE: " + getQueueStat().toStatsString();
	}

}
