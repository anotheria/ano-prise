package net.anotheria.anoprise.eventservice;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestEventChannelPushConsumerProxy {
	 @Test public void addWrongConsumer(){
		 EventChannelPushConsumerProxy proxy = new EventChannelPushConsumerProxy("foo");
		 try{
			 proxy.addConsumer(new Consumer());
			 fail("Exception expected");
		 }catch(Exception e){}
	 }
	 
	 @Test public void testErrorneousConsumer(){
		 EventChannelPushConsumerProxy proxy = new EventChannelPushConsumerProxy("foo");
		 PushConsumer c1 = new PushConsumer();
		 PushConsumer c2 = new PushConsumer();
		 
		 proxy.addConsumer(c1);
		 proxy.addConsumer(new ErrorThrowingPushConsumer());
		 proxy.addConsumer(c2);
		 
		 for (int i=0; i<10; i++){
			 proxy.pushEvent(new Event());
		 }
		 
		 assertEquals(10, c1.getCounter());
		 assertEquals(10, c2.getCounter());
	 }
	 
	 @Test public void testRemove(){
		 EventChannelPushConsumerProxy proxy = new EventChannelPushConsumerProxy("foo");
		 PushConsumer c1 = new PushConsumer();
		 PushConsumer c2 = new PushConsumer();
		 
		 proxy.addConsumer(c1);
		 proxy.addConsumer(c2);
		 
		 for (int i=0; i<10; i++){
			 proxy.pushEvent(new Event());
		 }
		 
		 assertEquals(10, c1.getCounter());
		 assertEquals(10, c2.getCounter());
		 
		 proxy.removeConsumer(c1);
		 
		 for (int i=0; i<10; i++){
			 proxy.pushEvent(new Event());
		 }
		 
		 assertEquals(10, c1.getCounter());
		 assertEquals(20, c2.getCounter());

	 }
	 
	 @Test public void testPush(){
		 EventChannelPushConsumerProxy proxy = new EventChannelPushConsumerProxy("foo");
		 try{
			 proxy.push(new Event());
			 fail("exception expected");
		 }catch(Exception e){}
	 }

	 public static class Consumer implements EventServiceConsumer{
		 
	 }
	 
	 public static class ErrorThrowingPushConsumer implements EventServicePushConsumer{

		@Override
		public void push(Event e) {
			throw new RuntimeException("FOO");
		}
		 
	 }

	 public static class PushConsumer implements EventServicePushConsumer{

		 private int counter = 0;
			@Override
			public void push(Event e) {
				counter++;
			}
			
			public int getCounter(){
				return counter;
			}
			 
		 }
}
