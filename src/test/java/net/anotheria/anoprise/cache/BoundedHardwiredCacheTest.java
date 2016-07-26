package net.anotheria.anoprise.cache;

import org.junit.Test;

import static net.anotheria.anoprise.cache.CacheTestSettings.MAX_SIZE;

public class BoundedHardwiredCacheTest {
	@Test public void testBasicFunctionality() throws Exception{
		BoundedHardwiredCache<Integer, String> cache = new BoundedHardwiredCache<>(MAX_SIZE);
		BoundedCacheTester.testBasicFunctionality(cache);
	}
	
	@Test public void testOverwrite() throws Exception{
		BoundedHardwiredCache<Integer, String> cache = new BoundedHardwiredCache<>(MAX_SIZE);
		BoundedCacheTester.testOverwrite(cache);
	}
	
	//no need to test rollover in a rollover unsupporting cache.

	@Test public void testConcurrency() throws Exception{
		BoundedHardwiredCache<Integer, String> cache = new BoundedHardwiredCache<>(MAX_SIZE);
		BoundedCacheTester.tryToCorruptInternalStructures(cache);
		
	}

	@Test public void testCompetition() throws Exception{
		BoundedHardwiredCache<Integer, String> cache = new BoundedHardwiredCache<>(MAX_SIZE);
		BoundedCacheTester.writeCompetion(cache);
		
	}
}
