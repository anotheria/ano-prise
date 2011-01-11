package net.anotheria.anoprise.queue;

/**
 * The factory interface for a queue.
 * @author lrosenberg
 *
 * @param <T>
 */
public interface EnterpriseQueueFactory<T> {
	/**
	 * Creates a new queue of the given size.
	 * @param size
	 * @return
	 */
	EnterpriseQueue<T> createQueue(int size);
}
