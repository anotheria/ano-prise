package net.anotheria.anoprise.eventservice;

/**
 * TODO Please remind lrosenberg to comment this class.
 * @author lrosenberg
 * Created on 22.09.2004
 */
public interface EventServicePushConsumer extends EventServiceConsumer{
	void push(Event e);
}
