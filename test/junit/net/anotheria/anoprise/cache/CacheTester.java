package net.anotheria.anoprise.cache;

import static net.anotheria.anoprise.cache.CacheTestSuite.MAX_SIZE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.CountDownLatch;


public class CacheTester {
	public static void testBasicFunctionality(Cache<Integer, String> cache) throws Exception{
		
		for (int i=0; i<MAX_SIZE; i++){
			cache.put(i,""+i);
		}
		
		for (int i=0; i<MAX_SIZE; i++){
			String s = cache.get(i);
			assertNotNull(s);
			assertEquals(i, Integer.parseInt(s));
		}
		
		//testing clear
		cache.clear();
		for (int i=0; i<MAX_SIZE; i++){
			String s = cache.get(i);
			assertNull(s);
		}
		
		//put again.
		for (int i=0; i<MAX_SIZE; i++){
			cache.put(i,""+i);
		}
		
		for (int i=0; i<MAX_SIZE; i++){
			cache.remove(i);
		}
		for (int i=0; i<MAX_SIZE; i++){
			String s = cache.get(i);
			assertNull(s);
		}
	
		cache.remove(-1);//this should have no effect.
		
	}
	
	public static void testOverwrite(Cache<Integer, String> cache) throws Exception{
		for (int i=0; i<MAX_SIZE; i++){
			cache.put(i,""+i);
		}
		//cache is full now, fill it again.
		for (int i=0; i<MAX_SIZE; i++){
			cache.put(i,""+(i+MAX_SIZE));
		}

		for (int i=0; i<MAX_SIZE; i++){
			String s = cache.get(i);
			s = cache.get(i);
			assertNotNull(s);
			assertEquals(i+MAX_SIZE, Integer.parseInt(s));
		}

	}

	public static void testRollover(Cache<Integer, String> cache) throws Exception{
		for (int i=0; i<MAX_SIZE; i++){
			cache.put(i,""+i);
		}
		//cache is full now, fill it again.
		for (int i=0; i<MAX_SIZE; i++){
			cache.put(i+MAX_SIZE,""+(i+MAX_SIZE));
		}

		for (int i=0; i<MAX_SIZE; i++){
			int key1 = i;
			int key2 = i+MAX_SIZE;
			String s = cache.get(key1);
			assertNull(s);
			s = cache.get(key2);
			assertNotNull(s);
			assertEquals(key2, Integer.parseInt(s));
		}

	}
	
	public static void tryToCorruptInternalStructures(final Cache<Integer, String> cache) throws Exception{
		final int parallelThreadCount = CacheTestSuite.PARALLEL_THREAD_COUNT;
		final int operationCount = CacheTestSuite.REPETITION_COUNT;
		final CountDownLatch startLatch = new CountDownLatch(1);
		final CountDownLatch stopLatch = new CountDownLatch(parallelThreadCount);
		
		//first fill the cache
		//for (int i=0; i<MAX_SIZE; i++){
		//	cache.put(i,""+i);
		//}
		
		
		final Random rnd = new Random(System.currentTimeMillis());
		final RunnerStats totals = new RunnerStats("totals", 0);
		Thread threads[] = new Thread[parallelThreadCount];
		for (int t = 0; t<parallelThreadCount; t++ ){
			threads[t] = new Thread(new Runnable() {
				
				@Override
				public void run() {
					try{
						startLatch.await();
						int modulo = rnd.nextInt(10); 
						int loops = operationCount / MAX_SIZE;
						RunnerStats stats = new RunnerStats(Thread.currentThread().getName(), modulo);
						for (int l=0; l<loops; l++){
							for (int i=0; i<MAX_SIZE; i++){
								try{
									if (i%10==modulo){
										cache.remove(i);
										stats.addRemove();
									}else{
										String value = cache.get(i);
										stats.addRead();
										if (value==null){
											stats.addMiss();
											cache.put(i, ""+i);
											stats.addWrite();
										}else{
											stats.addHit();
											if (!(Integer.parseInt(value)==i))
												stats.addError();
											//assertEquals(i, Integer.parseInt(value));
										}
									}
								}catch(Exception e){
									stats.addError();
								}
							}
						}
						totals.add(stats);
						System.out.println("Thread finished: "+stats);
						stopLatch.countDown();
					}catch(InterruptedException e){}
				}
			});
			threads[t].setName("Runner "+t);
			threads[t].start();
			
		}
		
		System.out.println("Starting all threads");
		long startTime = System.nanoTime();
		startLatch.countDown();
		
		stopLatch.await();
		long endTime = System.nanoTime();
		
		//perform sanity check
		int emptySpots = 0;
		for (int i=0; i<MAX_SIZE; i++){
			String v = cache.get(i);
			if (v!=null){
				assertEquals(i, Integer.parseInt(v));
			}else{
				emptySpots++;
			}
		}
		
		
		long timeInMs = (endTime-startTime)/1000/1000;
		System.out.println("All threads finished: totals "+totals+" empty spots: "+emptySpots+" of "+MAX_SIZE);
		System.out.println("Performed "+totals.requestCount() +" requests on ("+cache+") in "+timeInMs+" ms, performance: "+totals.requestCount()/timeInMs+" operations per millisecond");
		System.out.println("Stats: "+cache.getCacheStats().toStatsString());
		
	}
	
	public static void writeCompetion(final Cache<Integer, String> cache) throws Exception{
		final int parallelThreadCount = CacheTestSuite.PARALLEL_THREAD_COUNT;
		final int operationCount = CacheTestSuite.REPETITION_COUNT;
		final CountDownLatch startLatch = new CountDownLatch(1);
		final CountDownLatch stopLatch = new CountDownLatch(parallelThreadCount);
		
		final Random rnd = new Random(System.currentTimeMillis());
		final RunnerStats totals = new RunnerStats("totals", 0);
		Thread threads[] = new Thread[parallelThreadCount];
		

		final int loops = operationCount / MAX_SIZE;

		
		for (int t = 0; t<parallelThreadCount; t++ ){
			threads[t] = new Thread(new Runnable() {
				
				@Override
				public void run() {
					int identity = rnd.nextInt(100);
					String sIdentity = ""+identity;
					boolean doWrite = false;
					try{
						RunnerStats stats = new RunnerStats(Thread.currentThread().getName(), identity);
						startLatch.await();
						for (int l=0; l<loops; l++){
							int step = rnd.nextInt(3)+1;
							//System.out.println(sIdentity+", "+step);
							for (int i=0; i<MAX_SIZE; i+=step){
								try{
									String value = cache.get(i);
									stats.addRead();
									if (value!=null && value.equals(sIdentity))
										stats.addHit();
									else{
										stats.addMiss();
										if (doWrite){
											cache.put(i, sIdentity);
											stats.addWrite();
										}
										doWrite = !doWrite;
									}
									
								}catch(Exception e){
									e.printStackTrace();
									stats.addError();
								}
							}
						}
						totals.add(stats);
						stopLatch.countDown();
						System.out.println("Thread finished: "+stats);
					}catch(InterruptedException e){}
				}
			});
			threads[t].setName("Runner "+t);
			threads[t].start();
			
		}
		
		
		System.out.println("Starting all threads");
		long startTime = System.nanoTime();
		startLatch.countDown();
		
		stopLatch.await();
		long endTime = System.nanoTime();
		
		//perform sanity check
		int emptySpots = 0;
		
		HashMap<String, Integer> countersByIdentity = new HashMap<String, Integer>();
		
		for (int i=0; i<MAX_SIZE; i++){
			String v = cache.get(i);
			if (v!=null){
				Integer counter = countersByIdentity.get(v);
				if (counter==null)
					counter = 1;
				else
					counter = counter + 1;
				countersByIdentity.put(v,counter);
			}else{
				emptySpots++;
			}
		}
		
		long timeInMs = (endTime-startTime)/1000/1000;
		System.out.println("All threads finished: totals "+totals+" empty spots: "+emptySpots+" of "+MAX_SIZE);
		System.out.println("Performed "+totals.requestCount() +" requests on ("+cache+") in "+timeInMs+" ms, performance: "+totals.requestCount()/timeInMs+" operations per millisecond");
		System.out.println("Stats: "+cache.getCacheStats().toStatsString());
		System.out.println("Counters by Identity: "+countersByIdentity);
	}
	
	static class RunnerStats{
		private int modulo;
		private String name;
		
		private int readCount;
		private int writeCount;
		private int removeCount;
		private int errorCount;
		private int missCount;
		private int hitCount;
		
		public RunnerStats(String aName, int aModulo){
			name = aName;
			modulo = aModulo;
			readCount = writeCount = removeCount = errorCount = missCount = 0; 
		}
		
		public synchronized void add(RunnerStats anotherStats){
			readCount += anotherStats.readCount;
			writeCount += anotherStats.writeCount;
			removeCount += anotherStats.removeCount;
			errorCount += anotherStats.errorCount;
			missCount += anotherStats.missCount;
			hitCount += anotherStats.hitCount;
		}
		
		public String toString(){
			return "Runner "+name+", mod: "+modulo+", Stats: "+getStatsString();
		}
		
		public String getStatsString(){
			return "Read: "+readCount+" Hit: "+hitCount+", Miss: "+missCount+", Write: "+writeCount+", Remove: "+removeCount+", Error: "+errorCount+" HR: "+((double)hitCount/readCount)+", WR: "+((double)writeCount/readCount);
		}
		
		void addRead(){
			readCount++;
		}
		
		void addWrite(){
			writeCount++;
		}
		
		void addRemove(){
			removeCount++;
		}
		
		void addError(){
			errorCount++;
		}
		
		void addMiss(){
			missCount++;
		}
		
		void addHit(){
			hitCount++;
		}
		
		int requestCount(){
			return readCount+writeCount+removeCount;
		}
	}
	
	
	
}
