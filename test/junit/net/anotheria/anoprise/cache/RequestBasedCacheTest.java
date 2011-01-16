package net.anotheria.anoprise.cache;

import org.apache.log4j.BasicConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

public class RequestBasedCacheTest {
	
	@BeforeClass public static void initlog4j(){
		BasicConfigurator.configure();
	}
	
	@Test public void testBasicFunctionality() throws Exception{
		RequestBasedCache<Integer, String> cache = new RequestBasedCache<Integer, String>();
		cache.clear();
		CacheTester.testBasicFunctionality(cache);
	}
	
	@Test public void testOverwrite() throws Exception{
		RequestBasedCache<Integer, String> cache = new RequestBasedCache<Integer, String>();
		cache.clear();
		CacheTester.testOverwrite(cache);
	}
	
	//since the cache is thread based, it doesn't support rollover or fixed size.
	
	@Test public void testConcurrency() throws Exception{
		RequestBasedCache<Integer, String> cache = new RequestBasedCache<Integer, String>();
		cache.clear();
		CacheTester.tryToCorruptInternalStructures(cache);
	}

	@Test public void testCompetion() throws Exception{
		RequestBasedCache<Integer, String> cache = new RequestBasedCache<Integer, String>();
		cache.clear();
		CacheTester.writeCompetion(cache);
	}

}	
 