package net.anotheria.anoprise.processor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import net.anotheria.util.ThreadUtils;

import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class QueuedMultiProcessorTest {
	
	public static final int QUEUE_SIZE = 50;
	public static final int PROCESSOR_CHANNELS = 5;
	public static final int SLEEP_TIME = 50;	
	public static final int REQUESTS = 500;
	
	
	private AtomicLong elementCount;
	private Random rnd;
	private Set<Integer> worksRegression; 
	
	
	@BeforeClass public static void setup(){
		BasicConfigurator.configure();
//		DOMConfigurator.configure("etc/appdata/log4j.xml");
	}
	
	@Before public void reset(){
		elementCount = new AtomicLong();
		rnd = new Random(System.currentTimeMillis());
		worksRegression = Collections.newSetFromMap(new ConcurrentHashMap<Integer, Boolean>(REQUESTS));
	}
	
	@Test public void dontWaitTest() throws InterruptedException{
		CountDownLatch worksDoneLatch = new CountDownLatch(REQUESTS);
		TestWorker worker = new TestWorker(worksDoneLatch);
		
		QueuedMultiProcessor<Integer> processor = new QueuedMultiProcessorBuilder<Integer>().setQueueSize(QUEUE_SIZE).setSleepTime(SLEEP_TIME).setProcessorChannels(PROCESSOR_CHANNELS).build("dontWaitTest", worker);
		processor.start();
		
		int fails = 0;
		
		for(int i = 0; i < REQUESTS; i++){
			try {
				processor.addToQueueDontWait(new Integer(i), 2, 20);
				worksRegression.add(new Integer(i));
			} catch (UnrecoverableQueueOverflowException ignored) {
				fails++;
				worksDoneLatch.countDown();
			}
		}
		worksDoneLatch.await(30, TimeUnit.SECONDS);
		assertTrue(fails > 0);
		assertEquals(REQUESTS - fails, elementCount.get());
		assertEquals(0, worksRegression.size());
//		System.out.println("STATS dontWaitTest: " + processor.getStatsString());
	} 
	
	@Test public void waitTest() throws Exception{
		CountDownLatch worksDoneLatch = new CountDownLatch(REQUESTS);
		TestWorker worker = new TestWorker(worksDoneLatch);
		
		QueuedMultiProcessor<Integer> processor = new QueuedMultiProcessorBuilder<Integer>().setQueueSize(10).setSleepTime(SLEEP_TIME).setProcessorChannels(PROCESSOR_CHANNELS).build("waitTest", worker);
		processor.start();
		
		for(int i = 0; i < REQUESTS; i++){
			worksRegression.add(new Integer(i));
			processor.addToQueueAndWait(new Integer(i));
		}
		
		worksDoneLatch.await(30, TimeUnit.SECONDS);
		assertEquals(REQUESTS, elementCount.get());
		assertEquals(0, worksRegression.size());
//		System.out.println("STATS waitTest: " + processor.getStatsString());
	} 
	
	
	@Test(expected=UnrecoverableQueueOverflowException.class) public void queueOverflowTest() throws UnrecoverableQueueOverflowException{
		final Random rnd = new Random(System.currentTimeMillis());
		
		final ElementWorker<Integer> worker = new ElementWorker<Integer>() {
			@Override
			public void doWork(Integer workingElement) throws Exception {
				ThreadUtils.sleepIgnoreException(rnd.nextInt(100));
			}
		};		
		
		QueuedMultiProcessor<Integer> processor = new QueuedMultiProcessorBuilder<Integer>().setQueueSize(10).setSleepTime(SLEEP_TIME).setProcessorChannels(PROCESSOR_CHANNELS).build("queueOverflowTest", worker);
		processor.start();
		
		for(int i = 0; i < 200; i++){
			processor.addToQueueDontWait(new Integer(i), 2, 20);
		}
	}
	
	
	class TestWorker implements ElementWorker<Integer> {
		
		CountDownLatch worksDoneLatch;
		
		public TestWorker(CountDownLatch aWorksDoneLatch) {
			worksDoneLatch = aWorksDoneLatch;
		}
		
		@Override
		public void doWork(Integer workingElement) throws Exception {
			ThreadUtils.sleepIgnoreException(rnd.nextInt(100));
			elementCount.incrementAndGet();
			assertTrue(worksRegression.remove(workingElement));
			worksDoneLatch.countDown();
		}
	};
	
}
