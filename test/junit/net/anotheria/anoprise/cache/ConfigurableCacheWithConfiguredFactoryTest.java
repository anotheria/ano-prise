package net.anotheria.anoprise.cache;

import org.configureme.ConfigurationManager;
import org.configureme.environments.DynamicEnvironment;
import org.junit.BeforeClass;
import org.junit.Test;

public class ConfigurableCacheWithConfiguredFactoryTest {
	
	@BeforeClass public static void initconfigurationmanager(){
		ConfigurationManager.INSTANCE.setDefaultEnvironment(new DynamicEnvironment("unittest"));
	}
	
	@Test public void testBasicFunctionality() throws Exception{
		Cache<Integer, String> cache = Caches.createConfigurableCache("cachetest2");
		CacheTester.testBasicFunctionality(cache);
	}
	
	@Test public void testOverwrite() throws Exception{
		Cache<Integer, String> cache = Caches.createConfigurableCache("cachetest2");
		CacheTester.testOverwrite(cache);
	}
	
	@Test public void testRollover() throws Exception{
		Cache<Integer, String> cache = Caches.createConfigurableCache("cachetest2");
		CacheTester.testRollover(cache);
	}
	
	@Test public void testConcurrency() throws Exception{
		Cache<Integer, String> cache = Caches.createConfigurableCache("cachetest2");
		CacheTester.tryToCorruptInternalStructures(cache);
	}

	@Test public void testCompetion() throws Exception{
		Cache<Integer, String> cache = Caches.createConfigurableCache("cachetest2");
		CacheTester.writeCompetion(cache);
	}
}	
