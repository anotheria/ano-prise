package net.anotheria.anoprise.cache;

import org.apache.log4j.BasicConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

import static net.anotheria.anoprise.cache.CacheTestSuite.START_SIZE;
import static net.anotheria.anoprise.cache.CacheTestSuite.MAX_SIZE;

public class RoundRobinSoftReferenceExperimentalCacheTest {
	
	@BeforeClass public static void initlog4j(){
		BasicConfigurator.configure();
	}
	
	@Test public void testBasicFunctionality() throws Exception{
		RoundRobinSoftReferenceExperimentalCache<Integer, String> cache = new RoundRobinSoftReferenceExperimentalCache<Integer, String>(START_SIZE, MAX_SIZE);
		CacheTester.testBasicFunctionality(cache);
	}
	
	@Test public void testOverwrite() throws Exception{
		RoundRobinSoftReferenceExperimentalCache<Integer, String> cache = new RoundRobinSoftReferenceExperimentalCache<Integer, String>(START_SIZE, MAX_SIZE);
		CacheTester.testOverwrite(cache);
	}
	
	@Test public void testRollover() throws Exception{
		RoundRobinSoftReferenceExperimentalCache<Integer, String> cache = new RoundRobinSoftReferenceExperimentalCache<Integer, String>(START_SIZE, MAX_SIZE);
		CacheTester.testRollover(cache);
	}
	
	@Test public void testConcurrency() throws Exception{
		RoundRobinSoftReferenceExperimentalCache<Integer, String> cache = new RoundRobinSoftReferenceExperimentalCache<Integer, String>(START_SIZE, MAX_SIZE);
		CacheTester.tryToCorruptInternalStructures(cache);
		
	}
}	
