package net.anotheria.anoprise.cache;

import org.apache.log4j.BasicConfigurator;
import org.configureme.ConfigurationManager;
import org.configureme.environments.DynamicEnvironment;
import org.junit.BeforeClass;
import org.junit.Test;

public class ConfigurableRoundRobinHardwiredCacheTest {
	
	@BeforeClass public static void initlog4j(){
		BasicConfigurator.configure();
	}
	
	@BeforeClass public static void initconfigurationmanager(){
		ConfigurationManager.INSTANCE.setDefaultEnvironment(new DynamicEnvironment("unittest"));
	}
	
	@Test public void testBasicFunctionality() throws Exception{
		Cache<Integer, String> cache = Caches.createConfigurableHardwiredCache("cachetest");
		CacheTester.testBasicFunctionality(cache);
	}
	
	@Test public void testOverwrite() throws Exception{
		Cache<Integer, String> cache = Caches.createConfigurableHardwiredCache("cachetest");
		CacheTester.testOverwrite(cache);
	}
	
	@Test public void testRollover() throws Exception{
		Cache<Integer, String> cache = Caches.createConfigurableHardwiredCache("cachetest");
		CacheTester.testRollover(cache);
	}
	
	@Test public void testConcurrency() throws Exception{
		Cache<Integer, String> cache = Caches.createConfigurableHardwiredCache("cachetest");
		CacheTester.tryToCorruptInternalStructures(cache);
	}

	@Test public void testCompetion() throws Exception{
		Cache<Integer, String> cache = Caches.createConfigurableHardwiredCache("cachetest");
		CacheTester.writeCompetion(cache);
	}
}	
