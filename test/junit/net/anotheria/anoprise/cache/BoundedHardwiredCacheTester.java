package net.anotheria.anoprise.cache;

import static net.anotheria.anoprise.cache.CacheTestSuite.MAX_SIZE;

import org.apache.log4j.BasicConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

public class BoundedHardwiredCacheTester {
	@BeforeClass public static void initlog4j(){
		BasicConfigurator.configure();
	}
	
	@Test public void testBasicFunctionality() throws Exception{
		BoundedHardwiredCache<Integer, String> cache = new BoundedHardwiredCache<Integer, String>(MAX_SIZE);
		BoundedCacheTest.testBasicFunctionality(cache);
	}
	
	@Test public void testOverwrite() throws Exception{
		BoundedHardwiredCache<Integer, String> cache = new BoundedHardwiredCache<Integer, String>(MAX_SIZE);
		BoundedCacheTest.testOverwrite(cache);
	}
	
	//no need to test rollover in a rollover unsupporting cache.

	@Test public void testConcurrency() throws Exception{
		BoundedHardwiredCache<Integer, String> cache = new BoundedHardwiredCache<Integer, String>(MAX_SIZE);
		BoundedCacheTest.tryToCorruptInternalStructures(cache);
		
	}

	@Test public void testCompetition() throws Exception{
		BoundedHardwiredCache<Integer, String> cache = new BoundedHardwiredCache<Integer, String>(MAX_SIZE);
		BoundedCacheTest.writeCompetion(cache);
		
	}
}
