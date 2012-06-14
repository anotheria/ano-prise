package net.anotheria.anoprise.cache;

import org.apache.log4j.BasicConfigurator;
import org.configureme.ConfigurationManager;
import org.configureme.environments.DynamicEnvironment;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author ivanbatura
 * @since: 14.06.12
 */
public class ConfigurableRoundRobinSoftReferenceCacheWithFailoverSupportTest {
	@BeforeClass
	public static void initlog4j() {
		BasicConfigurator.configure();
	}

	@BeforeClass
	public static void initconfigurationmanager() {
		ConfigurationManager.INSTANCE.setDefaultEnvironment(new DynamicEnvironment("unittest"));
	}

	@Test
	public void testBasicFunctionality() throws Exception {
		Cache<Integer, String> cache = Caches.createConfigurableSoftReferenceCacheFailoverSupportCache("cachetest3", null);
		CacheTester.testBasicFunctionality(cache);
	}

	@Test
	public void testOverwrite() throws Exception {
		Cache<Integer, String> cache = Caches.createConfigurableSoftReferenceCacheFailoverSupportCache("cachetest3", null);
		CacheTester.testOverwrite(cache);
	}

	@Test
	public void testRollover() throws Exception {
		Cache<Integer, String> cache = Caches.createConfigurableSoftReferenceCacheFailoverSupportCache("cachetest3", null);
		CacheTester.testRollover(cache);
	}

	@Test
	public void testConcurrency() throws Exception {
		Cache<Integer, String> cache = Caches.createConfigurableSoftReferenceCacheFailoverSupportCache("cachetest3", null);
		CacheTester.tryToCorruptInternalStructures(cache);
	}

	@Test
	public void testCompetion() throws Exception {
		Cache<Integer, String> cache = Caches.createConfigurableSoftReferenceCacheFailoverSupportCache("cachetest3", null);
		CacheTester.writeCompetion(cache);
	}

	@Test
	public void testFailover() throws Exception {
		Cache<Integer, String> cache = Caches.createConfigurableSoftReferenceCacheFailoverSupportCache("cachetest3", null);
		CacheTester.testFailOverFunctionality(cache);
	}
}
