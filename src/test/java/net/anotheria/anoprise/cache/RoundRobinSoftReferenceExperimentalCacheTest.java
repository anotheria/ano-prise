package net.anotheria.anoprise.cache;

import org.junit.Test;

import static net.anotheria.anoprise.cache.CacheTestSettings.MAX_SIZE;
import static net.anotheria.anoprise.cache.CacheTestSettings.START_SIZE;

public class RoundRobinSoftReferenceExperimentalCacheTest {
	@Test public void testBasicFunctionality() throws Exception{
		RoundRobinSoftReferenceExperimentalCache<Integer, String> cache = new RoundRobinSoftReferenceExperimentalCache<>(START_SIZE, MAX_SIZE);
		CacheTester.testBasicFunctionality(cache);
	}
	
	@Test public void testOverwrite() throws Exception{
		RoundRobinSoftReferenceExperimentalCache<Integer, String> cache = new RoundRobinSoftReferenceExperimentalCache<>(START_SIZE, MAX_SIZE);
		CacheTester.testOverwrite(cache);
	}
	
	@Test public void testRollover() throws Exception{
		RoundRobinSoftReferenceExperimentalCache<Integer, String> cache = new RoundRobinSoftReferenceExperimentalCache<>(START_SIZE, MAX_SIZE);
		CacheTester.testRollover(cache);
	}
	
	@Test public void testConcurrency() throws Exception{
		RoundRobinSoftReferenceExperimentalCache<Integer, String> cache = new RoundRobinSoftReferenceExperimentalCache<>(START_SIZE, MAX_SIZE);
		CacheTester.tryToCorruptInternalStructures(cache);
	}

	@Test public void testCompetition() throws Exception{
		RoundRobinSoftReferenceExperimentalCache<Integer, String> cache = new RoundRobinSoftReferenceExperimentalCache<>(START_SIZE, MAX_SIZE);
		CacheTester.writeCompetion(cache);
	}
}	
