package net.anotheria.anoprise.sessiondistributor;

import net.anotheria.anoprise.sessiondistributor.cache.SDCache;
import net.anotheria.anoprise.sessiondistributor.cache.SDCacheUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * Junit for net.anotheria.anoprise.sessiondistributor.cache stuff.
 * Simply  test Cache and it utils functionality!
 *
 * @author h3ll
 */
public class CacheUtilTest {


	@Test
	public void testFlow() {
		//creating some cache!
		SDCache cache = SDCacheUtil.createCache("1");
		Assert.assertNotNull("Is null", cache);

		String id = cache.createSession("123123qweqweqweqweqweqeasflk");
		Assert.assertNotNull("is null", id);

		try {
			DistributedSessionVO session = cache.getSession(id);
			Assert.assertNotNull("Is null", session);
			Assert.assertEquals(" Not equals", session.getName(), id);


			// lets  persist it!!
			SDCacheUtil.save(cache);
			int elementsCount = cache.getCount();
			// kill current Cache!
			//noinspection UnusedAssignment
			cache = null;

			// lets  enable integration - and try to restore from FS!
			//enable clustering
			SessionDistributorServiceConfig.getInstance().setMultipleInstancesEnabled(true);
			//enable FS persistence of sessions
			SessionDistributorServiceConfig.getInstance().setWrightSessionsToFsOnShutdownEnabled(true);

			//  trying to read same session!!
			cache = SDCacheUtil.createCache("1");

			DistributedSessionVO session2 = cache.getSession(id);
			cache.updateCallTime(id);
			Assert.assertNotNull("Is null", session2);
			Assert.assertEquals(" Not properly restored!", session2.getName(), session.getName());

			String id5 = cache.createSession("123123qweqweqweqweqweqeasflkqweqweq");
			cache.updateCallTime(id5);

			Assert.assertNotNull("is null", id5);

			//remove all stuff!
			cache.removeSession(id);



			cache.removeSession(id5);

			//try to read not existing session
			try {
				cache.getSession(id);
				Assert.fail("Already deleted");
			} catch (NoSuchDistributedSessionException e) {
			}


		} catch (NoSuchDistributedSessionException e) {
			Assert.fail("can't happen!!!");
		}
	}
}
