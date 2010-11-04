package net.anotheria.anoprise.eventservice;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import static org.junit.Assert.*;

public class EventChannelPushSupplierProxyTest {
	@Test public void testOmmitedFunctionallity(){
		EventChannelPushSupplierProxy p = new EventChannelPushSupplierProxy("foo");
		try{
			p.addConsumer(new EventServiceConsumer() {
			});
			fail("expected exception");
		}catch(Exception e){}
		try{
			p.removeConsumer(new EventServiceConsumer() {
			});
			fail("expected exception");
		}catch(Exception e){}
	}
	
	@Test public void testLocalVsRemote(){
		EventChannelPushSupplierProxy p = new EventChannelPushSupplierProxy("foo");
		final AtomicInteger localCounter = new AtomicInteger();
		EventChannelConsumerProxy local = new EventChannelConsumerProxy() {
			
			@Override
			public boolean isLocal() {
				return true;
			}
			
			@Override
			public void pushEvent(Event e) {
				localCounter.incrementAndGet();
			}
		};
		final AtomicInteger remoteCounter = new AtomicInteger();
		EventChannelConsumerProxy remote = new EventChannelConsumerProxy() {
			
			@Override
			public boolean isLocal() {
				return false;
			}
			
			@Override
			public void pushEvent(Event e) {
				remoteCounter.incrementAndGet();
			}
		};
		
		p.addConsumerProxy(local);
		p.addConsumerProxy(remote);
		
		for (int i=0; i<10; i++)
			p.push(new Event());

		assertEquals(10, localCounter.get());
		assertEquals(10, remoteCounter.get());
		
		for (int i=0; i<10; i++)
			p.push(new Event(), true);
		
		assertEquals(20, localCounter.get());
		assertEquals(10, remoteCounter.get());

		p.removeConsumerProxy(local);
		for (int i=0; i<10; i++)
			p.push(new Event());
		
		assertEquals(20, localCounter.get());
		assertEquals(20, remoteCounter.get());
		
		p.removeConsumerProxy(remote);
		for (int i=0; i<10; i++)
			p.push(new Event());
		
		assertEquals(20, localCounter.get());
		assertEquals(20, remoteCounter.get());
	}
	
	@Test public void surviveException(){
		EventChannelPushSupplierProxy p = new EventChannelPushSupplierProxy("foo");
		EventChannelConsumerProxy proxy = new EventChannelConsumerProxy() {
			
			@Override
			public boolean isLocal() {
				return true;
			}
			
			@Override
			public void pushEvent(Event e) {
				throw new RuntimeException("boo");
			}
		};
		
		p.addConsumerProxy(proxy);
		for (int i=0; i<10; i++)
			p.push(new Event());
		
		//if we fail, exception will be thrown here
		
	}
}
