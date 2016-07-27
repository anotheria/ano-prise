package net.anotheria.anoprise.queue;

import net.anotheria.moskito.core.predefined.QueueStats;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;

/**
 * Completely adapts standard java queue to enterprise without any limitation of
 * usage.
 * 
 * @author dmetelin
 * 
 */
public class EnterpriseQueueAdapter<T> implements EnterpriseQueue<T> {

	private Queue<T> delegate;

	private int capacity;

	/**
	 * Listeners of the queue.
	 */
	private List<EnterpriseQueueListener> listeners;

	private QueueStats queueStats;

	/**
	 * Creates new adapter for a Queue
	 * 
	 * @param aDelegateQueue
	 *            underlying queue to delegate all queuing operations
	 */
	public EnterpriseQueueAdapter(Queue<T> aDelegateQueue) {
		this(aDelegateQueue, Integer.MAX_VALUE);
	}

	/**
	 * Creates new adapter for a BlockingQueue. Parameter aCapacity
	 * must be the same as BlockingQueue instance was constructed.
	 * 
	 * @param aDelegateQueue
	 *            underlying queue to delegate all queuing operations
	 * @param aCapacity
	 *            underlying queue capacity restriction. Has only information
	 *            aim and doesn't re-restrict any how underlying queue.
	 */
	public EnterpriseQueueAdapter(BlockingQueue<T> aDelegateQueue, int aCapacity) {
		this((Queue<T>) aDelegateQueue, aCapacity);
	}

	private EnterpriseQueueAdapter(Queue<T> aDelegateQueue, int aCapacity) {
		delegate = aDelegateQueue;
		capacity = aCapacity;
		listeners = new ArrayList<>();
		queueStats = new QueueStats(this.getClass().getSimpleName());
		queueStats.setTotalSize(aCapacity);
	}

	@Override
	public void addListener(EnterpriseQueueListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeListener(EnterpriseQueueListener listener) {
		listeners.remove(listener);
	}

	@Override
	public QueueStats getQueueStats() {
		return queueStats;
	}

	@Override
	public void add(T e) {
		if (!offer(e))
			throw new QueueOverflowException();
	}

	@Override
	public boolean offer(T e) {
		queueStats.addRequest();
		queueStats.setOnRequestLastSize(delegate.size());
		boolean enqueued = delegate.offer(e);
		if (enqueued)
			queueStats.addEnqueued();
		return enqueued;
	}

	@Override
	public T remove() {
		T ret = poll();
		if (ret == null) {
			throw new QueueEmptyException();
		}
		return ret;
	}

	@Override
	public T poll() {
		T ret = delegate.poll();
		if (ret == null)
			queueStats.addEmpty();
		else
			queueStats.addDequeued();
		return ret;
	}

	@Override
	public T peek() {
		T ret = delegate.peek();
		if (ret == null)
			queueStats.addEmpty();
		return ret;
	}

	@Override
	public int size() {
		return delegate.size();
	}

	@Override
	public boolean isEmpty() {
		return delegate.isEmpty();
	}

	@Override
	public int capacity() {
		return capacity;
	}

	@Override
	public List<T> drain() {
		List<T> ret = new ArrayList<>();

		for (T element = delegate.poll(); element != null; element = delegate.poll())
			ret.add(element);

		return ret;
	}

}
