package net.anotheria.anoprise.cache;

import org.junit.Test;

import static net.anotheria.anoprise.cache.CacheTestSettings.CURRENT_INSTANCE_NUMBER;
import static net.anotheria.anoprise.cache.CacheTestSettings.INSTANCE_AMOUNT;
import static net.anotheria.anoprise.cache.CacheTestSettings.MAX_SIZE;
import static net.anotheria.anoprise.cache.CacheTestSettings.START_SIZE;

/**
 * Unit test for net.anotheria.anoprise.cache.RoundRobinSoftReferenceFailoverSupportCache
 *
 * @author ivanbatura
 */
public class FailoverCacheTest {

	@Test
	public void testBasicFunctionality() throws Exception {
		FailoverCache<Integer, String> cache = new FailoverCache<>("test", 0, 0, null, new RoundRobinSoftReferenceCache<Integer, String>(START_SIZE, MAX_SIZE));
		CacheTester.testBasicFunctionality(cache);
	}

	@Test
	public void testOverwrite() throws Exception {
		FailoverCache<Integer, String> cache = new FailoverCache<>("test", 0, 0, null, new RoundRobinSoftReferenceCache<Integer, String>(START_SIZE, MAX_SIZE));
		CacheTester.testOverwrite(cache);
	}

	@Test
	public void testRollover() throws Exception {
		FailoverCache<Integer, String> cache = new FailoverCache<>("test", 0, 0, null, new RoundRobinSoftReferenceCache<Integer, String>(START_SIZE, MAX_SIZE));
		CacheTester.testRollover(cache);
	}

	@Test
	public void testConcurrency() throws Exception {
		FailoverCache<Integer, String> cache = new FailoverCache<>("test", 0, 0, null, new RoundRobinSoftReferenceCache<Integer, String>(START_SIZE, MAX_SIZE));
		CacheTester.tryToCorruptInternalStructures(cache);
	}

	@Test
	public void testFailOverFunctionality() throws Exception {
		FailoverCache<Integer, String> cache = new FailoverCache<>("test", INSTANCE_AMOUNT, CURRENT_INSTANCE_NUMBER, null, new RoundRobinSoftReferenceCache<Integer, String>(START_SIZE, MAX_SIZE));
		CacheTester.testFailOverFunctionality(cache);
	}

	@Test
	public void testCompetition() throws Exception {
		FailoverCache<Integer, String> cache = new FailoverCache<>("test", 0, 0, null, new RoundRobinSoftReferenceCache<Integer, String>(START_SIZE, MAX_SIZE));
		CacheTester.writeCompetion(cache);
	}

}
