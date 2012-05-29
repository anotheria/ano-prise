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
public class RoundRobinSoftReferenceFailoverSupportCacheTest {

	@BeforeClass
	public static void initlog4j() {
		BasicConfigurator.configure();
	}

	@Test
	public void testBasicFunctionality() throws Exception {
		RoundRobinSoftReferenceFailoverSupportCache<Integer, String> cache = new RoundRobinSoftReferenceFailoverSupportCache<Integer, String>(START_SIZE, MAX_SIZE, SERVICE_NODE_AMOUNT, REGISTRATION_NAME_PROVIDER);
		CacheTester.testBasicFunctionality(cache);
	}

	@Test
	public void testOverwrite() throws Exception {
		RoundRobinSoftReferenceFailoverSupportCache<Integer, String> cache = new RoundRobinSoftReferenceFailoverSupportCache<Integer, String>(START_SIZE, MAX_SIZE, SERVICE_NODE_AMOUNT, REGISTRATION_NAME_PROVIDER);
		CacheTester.testOverwrite(cache);
	}

	@Test
	public void testRollover() throws Exception {
		RoundRobinSoftReferenceFailoverSupportCache<Integer, String> cache = new RoundRobinSoftReferenceFailoverSupportCache<Integer, String>(START_SIZE, MAX_SIZE, SERVICE_NODE_AMOUNT, REGISTRATION_NAME_PROVIDER);
		CacheTester.testRollover(cache);
	}

	@Test
	public void testConcurrency() throws Exception {
		RoundRobinSoftReferenceFailoverSupportCache<Integer, String> cache = new RoundRobinSoftReferenceFailoverSupportCache<Integer, String>(START_SIZE, MAX_SIZE, SERVICE_NODE_AMOUNT, REGISTRATION_NAME_PROVIDER);
		CacheTester.tryToCorruptInternalStructures(cache);
	}

	@Test
	public void testFailOverFunctionality() throws Exception {
		RoundRobinSoftReferenceFailoverSupportCache<Integer, String> cache = new RoundRobinSoftReferenceFailoverSupportCache<Integer, String>(START_SIZE, MAX_SIZE, SERVICE_NODE_AMOUNT, REGISTRATION_NAME_PROVIDER);
		CacheTester.testFailOverFunctionality(cache);
	}

	@Test
	public void testCompetition() throws Exception {
		RoundRobinSoftReferenceFailoverSupportCache<Integer, String> cache = new RoundRobinSoftReferenceFailoverSupportCache<Integer, String>(START_SIZE, MAX_SIZE, SERVICE_NODE_AMOUNT, REGISTRATION_NAME_PROVIDER);
		CacheTester.writeCompetion(cache);
	}

}
