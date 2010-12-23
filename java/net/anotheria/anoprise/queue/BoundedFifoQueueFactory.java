package net.anotheria.anoprise.queue;

import java.util.concurrent.ArrayBlockingQueue;

public class BoundedFifoQueueFactory<E> implements EnterpriseQueueFactory<E>{

	@Override
	public EnterpriseQueue<E> createQueue(int capacity) {
		ArrayBlockingQueue<E> delegate = new ArrayBlockingQueue<E>(capacity);
		return new EnterpriseQueueAdapter<E>(delegate, capacity);
	}

}
