package net.anotheria.anoprise.sessiondistributor;

import net.anotheria.anoprise.fs.*;
import net.anotheria.anoprise.sessiondistributor.cache.SDCache;
import net.anotheria.anoprise.sessiondistributor.cache.SDCacheUtil;
import org.junit.*;

/**
 * Junit for net.anotheria.anoprise.sessiondistributor.cache stuff.
 * Simply  test Cache and it utils functionality!
 *
 * @author h3ll
 */
public class CacheUtilTest {


	private static final String NODE_0_VALUE = "0";
	private static final String NODE_1_VALUE = "1";

	@BeforeClass
	public static void before() {
		FSServiceConfig config = null;
		try {
			config = new FSServiceConfig(SessionDistributorServiceConfig.getInstance().getSdSessionsFSRootFolder(), SessionDistributorServiceConfig.getInstance().getSdSessionsFileExtension());
			FSService<SDCache> fsPersistence = FSServiceFactory.createFSService(config);
			fsPersistence.delete(NODE_0_VALUE);
			fsPersistence.delete(NODE_1_VALUE);
		} catch (FSServiceConfigException e) {
			Assert.fail("Should not happen!" + e.getMessage());
		} catch (FSServiceException e) {
			Assert.fail("Should not happen!" + e.getMessage());
		}

	}

	@Before
	public void beforeM(){
		before();
	}

	@AfterClass
	public static void after() {
		before();
	}

	@Test
	public void testFlow() {
		//prevents ooE
		System.setProperty("JUNITTEST", String.valueOf(true));
		//creating some cache!
		SDCache cache = SDCacheUtil.createCache();
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
			cache = SDCacheUtil.createCache();

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

			SessionDistributorServiceConfig.getInstance().setMultipleInstancesEnabled(false);
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

	@Test
	public void testSyncInCluster() {
		SessionDistributorServiceConfig.getInstance().setMultipleInstancesEnabled(true);
		final String sessionId1 = "100";
		final String sessionId2 = "200";

		//prevent errors
		System.setProperty("JUNITTEST", String.valueOf(true));

		//Setting proper  Node ID  via system property!  before cache creation  --  0 id for this node
		System.setProperty(SessionDistributorServiceConfig.getInstance().getNodeIdSystemPropertyName(), NODE_0_VALUE);
		SDCache cacheInstance1 = SDCacheUtil.createCache();
		Assert.assertNotNull("Is null", cacheInstance1);

		//Setting proper  Node ID  via system property!  before cache creation  --  1 id for this node
		System.setProperty(SessionDistributorServiceConfig.getInstance().getNodeIdSystemPropertyName(), NODE_1_VALUE);
		SDCache cacheInstance2 = SDCacheUtil.createCache();
		Assert.assertNotNull("Is null", cacheInstance2);

		String id1 = cacheInstance1.createSession(sessionId1);
		Assert.assertEquals(id1, sessionId1);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			Assert.fail();
		}
		String id2 = cacheInstance1.createSession(sessionId2);
		Assert.assertEquals(id2, sessionId2);

		//try to get first session from secondCache
		try {
			DistributedSessionVO session1 = cacheInstance2.getSession(sessionId1);
		} catch (NoSuchDistributedSessionException e) {
			Assert.fail("Should not happen! Replication mistMatch!!!");
		}
		//try to get first session from firstCache!
		try {
			DistributedSessionVO session2 = cacheInstance1.getSession(sessionId2);
		} catch (NoSuchDistributedSessionException e) {
			Assert.fail("Should not happen! Replication mistMatch!!!");
		}
		Assert.assertEquals("Error", cacheInstance1.getCount(), cacheInstance2.getCount());
		Assert.assertEquals("Error", cacheInstance1.getSessions(), cacheInstance2.getSessions());


		// adding some attribute!!!!!
		final String attributeName = "att1";
		try {
			cacheInstance1.addAttribute(sessionId1, new DistributedSessionAttribute(attributeName, new byte[0]));
			DistributedSessionVO session = cacheInstance1.getSession(sessionId1);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				Assert.fail();
			}

			DistributedSessionVO session1 = cacheInstance2.getSession(sessionId1);
			Assert.assertTrue("Replication error! Attribute not present!!", session1.getDistributedAttributes().containsKey(attributeName));
			Assert.assertEquals("Last change time differs!", session.getLastChangeTime(), session1.getLastChangeTime());


		} catch (NoSuchDistributedSessionException e) {
			Assert.fail();
		}

		// set UserID
		final String userID = "userID";
		final String editorId = "editorId";
		try {
			cacheInstance1.updateSessionUserId(sessionId1, userID);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				Assert.fail();
			}
			cacheInstance1.updateSessionEditorId(sessionId1, editorId);

			DistributedSessionVO session = cacheInstance1.getSession(sessionId1);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				Assert.fail();
			}

			DistributedSessionVO session1 = cacheInstance2.getSession(sessionId1);
			Assert.assertEquals("Replication error! editor id not p[resent!!", session1.getEditorId(), editorId);
			Assert.assertEquals("Replication error! user id not p[resent!!", session1.getUserId(), userID);
			Assert.assertEquals("Last change time differs!", session.getLastChangeTime(), session1.getLastChangeTime());


		} catch (NoSuchDistributedSessionException e) {
			Assert.fail();
		}


		//Keep allive!
		try {
			cacheInstance1.updateCallTime(sessionId1);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				Assert.fail();
			}
			DistributedSessionVO session = cacheInstance1.getSession(sessionId1);
			DistributedSessionVO session1 = cacheInstance2.getSession(sessionId1);
			Assert.assertEquals("Last change time differs!", session.getLastChangeTime(), session1.getLastChangeTime());


		} catch (NoSuchDistributedSessionException e) {
			Assert.fail();
		}

		//Delete calls!
		try {
			cacheInstance1.removeSession(sessionId1);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				Assert.fail();
			}
			try {
				cacheInstance2.getSession(sessionId1);
				Assert.fail("Error! Was not deleted by Async call! - Cache2 session1");
			} catch (NoSuchDistributedSessionException e) {
			}
			cacheInstance2.removeSession(sessionId2);
			try {
				cacheInstance1.getSession(sessionId2);
				Assert.fail("Error! Was not deleted by Async call!  Cache1 session2");
			} catch (NoSuchDistributedSessionException e) {
			}
			Assert.assertEquals("Error! smth present", cacheInstance1.getCount(), 0);
			Assert.assertEquals("Error! smth present", cacheInstance2.getCount(), 0);
		} catch (NoSuchDistributedSessionException e) {
			Assert.fail();
		}
		SessionDistributorServiceConfig.getInstance().setMultipleInstancesEnabled(false);

	}


}
