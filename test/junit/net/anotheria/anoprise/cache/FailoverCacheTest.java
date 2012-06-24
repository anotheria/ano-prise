package net.anotheria.anoprise.cache;

import org.apache.log4j.BasicConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

import static net.anotheria.anoprise.cache.CacheTestSettings.*;

/**
 * Unit test for net.anotheria.anoprise.cache.RoundRobinSoftReferenceFailoverSupportCache
 *
 * @author ivanbatura
 */
public class FailoverCacheTest {

	@BeforeClass
	public static void initlog4j() {
		BasicConfigurator.configure();
	}

	@Test
	public void testBasicFunctionality() throws Exception {
		FailoverCache<Integer, String> cache = new FailoverCache<Integer, String>("test",0, 0, null,  new RoundRobinSoftReferenceCache<Integer, String>(START_SIZE,MAX_SIZE));
		CacheTester.testBasicFunctionality(cache);
	}

	@Test
	public void testOverwrite() throws Exception {
		FailoverCache<Integer, String> cache = new FailoverCache<Integer, String>("test",0, 0, null,  new RoundRobinSoftReferenceCache<Integer, String>(START_SIZE,MAX_SIZE));
		CacheTester.testOverwrite(cache);
	}

	@Test
	public void testRollover() throws Exception {
		FailoverCache<Integer, String> cache = new FailoverCache<Integer, String>("test",0, 0, null,  new RoundRobinSoftReferenceCache<Integer, String>(START_SIZE,MAX_SIZE));
		CacheTester.testRollover(cache);
	}

	@Test
	public void testConcurrency() throws Exception {
		FailoverCache<Integer, String> cache = new FailoverCache<Integer, String>("test",0, 0, null,  new RoundRobinSoftReferenceCache<Integer, String>(START_SIZE,MAX_SIZE));
		CacheTester.tryToCorruptInternalStructures(cache);
	}

	@Test
	public void testFailOverFunctionality() throws Exception {
		FailoverCache<Integer, String> cache = new FailoverCache<Integer, String>("test",INSTANCE_AMOUNT, CURRENT_INSTANCE_NUMBER, null,  new RoundRobinSoftReferenceCache<Integer, String>(START_SIZE,MAX_SIZE));
		CacheTester.testFailOverFunctionality(cache);
	}

	@Test
	public void testCompetition() throws Exception {
		FailoverCache<Integer, String> cache = new FailoverCache<Integer, String>("test",0, 0, null,  new RoundRobinSoftReferenceCache<Integer, String>(START_SIZE,MAX_SIZE));
		CacheTester.writeCompetion(cache);
	}

}
