package net.anotheria.anoprise.cache;

import static net.anotheria.anoprise.cache.CacheTestSuite.MAX_SIZE;
import static net.anotheria.anoprise.cache.CacheTestSuite.START_SIZE;

import org.apache.log4j.BasicConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

public class RoundRobinHardwiredCacheTest {
	
	@BeforeClass public static void initlog4j(){
		BasicConfigurator.configure();
	}
	
	@Test public void testBasicFunctionality() throws Exception{
		RoundRobinHardwiredCache<Integer, String> cache = new RoundRobinHardwiredCache<Integer, String>(START_SIZE, MAX_SIZE);
		CacheTester.testBasicFunctionality(cache);
	}
	
	@Test public void testOverwrite() throws Exception{
		RoundRobinHardwiredCache<Integer, String> cache = new RoundRobinHardwiredCache<Integer, String>(START_SIZE, MAX_SIZE);
		CacheTester.testOverwrite(cache);
	}
	
	@Test public void testRollover() throws Exception{
		RoundRobinHardwiredCache<Integer, String> cache = new RoundRobinHardwiredCache<Integer, String>(START_SIZE, MAX_SIZE);
		CacheTester.testRollover(cache);
	}
	
	@Test public void testConcurrency() throws Exception{
		RoundRobinHardwiredCache<Integer, String> cache = new RoundRobinHardwiredCache<Integer, String>(START_SIZE, MAX_SIZE);
		CacheTester.tryToCorruptInternalStructures(cache);
		
	}
}	
