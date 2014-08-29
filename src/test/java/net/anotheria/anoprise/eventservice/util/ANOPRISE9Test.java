package net.anotheria.anoprise.eventservice.util;

import net.anotheria.anoprise.eventservice.Event;

import org.junit.Test;
import static org.junit.Assert.fail;

//tests for https://jira.opensource.anotheria.net/browse/ANOPRISE-9
public class ANOPRISE9Test {
	@Test public void testNotStarted(){
		QueuedEventSender s1 = new QueuedEventSender("test", "foo");
		try{
			s1.push(new Event());
			fail("Illegal state exception expected!");
		}catch(IllegalStateException e){
			
		}catch(QueueFullException e){}
	}
	
	@Test public void testStarted(){
		QueuedEventSender s1 = new QueuedEventSender("test", "foo");
		s1.start();
		try{
			s1.push(new Event());
		}catch(IllegalStateException e){
			e.printStackTrace();
			fail("EventSender is started and shouldn't fail!");
		}catch(QueueFullException e){}
	}
		
}
