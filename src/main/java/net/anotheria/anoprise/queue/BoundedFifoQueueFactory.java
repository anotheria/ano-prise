package net.anotheria.anoprise.queue;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Factory to create bounded FIFO queue.
 * @author dmeltelin
 *
 */
public class BoundedFifoQueueFactory<E> implements EnterpriseQueueFactory<E>{

	@Override
	public EnterpriseQueue<E> createQueue(int capacity) {
		ArrayBlockingQueue<E> delegate = new ArrayBlockingQueue<>(capacity);
		return new EnterpriseQueueAdapter<>(delegate, capacity);
	}

}
