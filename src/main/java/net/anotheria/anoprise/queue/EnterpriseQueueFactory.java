package net.anotheria.anoprise.queue;

/**
 * The factory interface for a queue.
 * @author lrosenberg
 *
 */
public interface EnterpriseQueueFactory<T> {
	/**
	 * Creates a new queue of the given size.
	 */
	EnterpriseQueue<T> createQueue(int size);
}
