package net.anotheria.anoprise.cache;

import org.apache.log4j.BasicConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

import static net.anotheria.anoprise.cache.CacheTestSettings.START_SIZE;
import static net.anotheria.anoprise.cache.CacheTestSettings.MAX_SIZE;

public class RoundRobinSoftReferenceCacheTest {
	
	@BeforeClass public static void initlog4j(){
		BasicConfigurator.configure();
	}
	
	@Test public void testBasicFunctionality() throws Exception{
		RoundRobinSoftReferenceCache<Integer, String> cache = new RoundRobinSoftReferenceCache<Integer, String>(START_SIZE, MAX_SIZE);
		CacheTester.testBasicFunctionality(cache);
	}
	
	@Test public void testOverwrite() throws Exception{
		RoundRobinSoftReferenceCache<Integer, String> cache = new RoundRobinSoftReferenceCache<Integer, String>(START_SIZE, MAX_SIZE);
		CacheTester.testOverwrite(cache);
	}
	
	@Test public void testRollover() throws Exception{
		RoundRobinSoftReferenceCache<Integer, String> cache = new RoundRobinSoftReferenceCache<Integer, String>(START_SIZE, MAX_SIZE);
		CacheTester.testRollover(cache);
	}
	
	@Test public void testConcurrency() throws Exception{
		RoundRobinSoftReferenceCache<Integer, String> cache = new RoundRobinSoftReferenceCache<Integer, String>(START_SIZE, MAX_SIZE);
		CacheTester.tryToCorruptInternalStructures(cache);
	}

	@Test public void testCompetion() throws Exception{
		RoundRobinSoftReferenceCache<Integer, String> cache = new RoundRobinSoftReferenceCache<Integer, String>(START_SIZE, MAX_SIZE);
		CacheTester.writeCompetion(cache);
	}
}	

