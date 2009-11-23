package net.anotheria.anoprise.cache;

import static org.junit.Assert.*;
import static net.anotheria.anoprise.cache.CacheTestSuite.MAX_SIZE;

import java.util.Random;
import java.util.concurrent.CountDownLatch;


public class BoundedCacheTester {
	public static void testBasicFunctionality(BoundedCache<Integer, String> cache) throws Exception{
		
		for (int i=0; i<MAX_SIZE; i++){
			cache.offer(i,""+i);
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
			cache.offer(i,""+i);
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
	
	public static void testOverwrite(BoundedCache<Integer, String> cache) throws Exception{
		for (int i=0; i<MAX_SIZE; i++){
			cache.offer(i,""+i);
		}
		//cache is full now, fill it again.
		for (int i=0; i<MAX_SIZE; i++){
			cache.offer(i,""+(i+MAX_SIZE));
		}

		//unlike the unbounded cache this cache doesn't allow overwrite. so it will simply ignore everything written after it was full.
		
		for (int i=0; i<MAX_SIZE; i++){
			String s = cache.get(i);
			s = cache.get(i);
			assertNotNull(s);
			assertEquals(i, Integer.parseInt(s));
		}

	}

	public static void tryToCorruptInternalStructures(final BoundedCache<Integer, String> cache) throws Exception{
		int parallelThreadCount = 10;
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
											cache.offer(i, ""+i);
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

		System.err.println("Performed "+totals.requestCount() +" requests on ("+cache+") in "+timeInMs+" ms, performance: "+totals.requestCount()/timeInMs+" operations per millisecond");

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
