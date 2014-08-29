package net.anotheria.anoprise.cache;

import org.configureme.ConfigurationManager;
import org.configureme.environments.DynamicEnvironment;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestConfigurationReading {
	@Test public void testInDevelopment(){
		ConfigurationManager.INSTANCE.setDefaultEnvironment(new DynamicEnvironment("dev"));
		Cache<Integer,String> cache = Caches.createConfigurableSoftReferenceCache("cachetest");
		
		CacheController<Integer, String> controller = (CacheController<Integer, String>)cache;
		controller.getDetails();
		
		assertTrue(controller.isCacheOn());
		assertEquals(3000, controller.getStartSize());
		assertEquals(7000, controller.getMaxSize());
	}
	@Test public void test10K(){
		ConfigurationManager.INSTANCE.setDefaultEnvironment(new DynamicEnvironment("tenK"));
		Cache<Integer,String> cache = Caches.createConfigurableSoftReferenceCache("cachetest");
		
		CacheController<Integer, String> controller = (CacheController<Integer, String>)cache;
		controller.getDetails();
		
		assertTrue(controller.isCacheOn());
		assertEquals(10000, controller.getStartSize());
		assertEquals(10000, controller.getMaxSize());
	}
	@Test public void testOff(){
		ConfigurationManager.INSTANCE.setDefaultEnvironment(new DynamicEnvironment("off"));
		Cache<Integer,String> cache = Caches.createConfigurableSoftReferenceCache("cachetest");
		
		CacheController<Integer, String> controller = (CacheController<Integer, String>)cache;
		controller.getDetails();
		
		assertFalse(controller.isCacheOn());
		assertEquals(3000, controller.getStartSize());
		assertEquals(7000, controller.getMaxSize());
	}

	@Test public void testInDevelopmentHW(){
		ConfigurationManager.INSTANCE.setDefaultEnvironment(new DynamicEnvironment("dev"));
		Cache<Integer,String> cache = Caches.createConfigurableHardwiredCache("cachetest");
		
		CacheController<Integer, String> controller = (CacheController<Integer, String>)cache;
		controller.getDetails();
		
		assertTrue(controller.isCacheOn());
		assertEquals(3000, controller.getStartSize());
		assertEquals(7000, controller.getMaxSize());
	}
	@Test public void test10KHW(){
		ConfigurationManager.INSTANCE.setDefaultEnvironment(new DynamicEnvironment("tenK"));
		Cache<Integer,String> cache = Caches.createConfigurableHardwiredCache("cachetest");
		
		CacheController<Integer, String> controller = (CacheController<Integer, String>)cache;
		controller.getDetails();
		
		assertTrue(controller.isCacheOn());
		assertEquals(10000, controller.getStartSize());
		assertEquals(10000, controller.getMaxSize());
	}
	@Test public void testOffHW(){
		ConfigurationManager.INSTANCE.setDefaultEnvironment(new DynamicEnvironment("off"));
		Cache<Integer,String> cache = Caches.createConfigurableHardwiredCache("cachetest");
		
		CacheController<Integer, String> controller = (CacheController<Integer, String>)cache;
		controller.getDetails();
		
		assertFalse(controller.isCacheOn());
		assertEquals(3000, controller.getStartSize());
		assertEquals(7000, controller.getMaxSize());
	}
}
