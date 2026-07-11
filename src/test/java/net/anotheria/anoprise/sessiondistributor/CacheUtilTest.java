package net.anotheria.anoprise.sessiondistributor;

import net.anotheria.anoprise.fs.*;
import net.anotheria.anoprise.sessiondistributor.cache.SDCache;
import net.anotheria.anoprise.sessiondistributor.cache.SDCacheUtil;
import org.junit.jupiter.api.*;

/**
 * Junit for net.anotheria.anoprise.sessiondistributor.cache stuff.
 * Simply  test Cache and it utils functionality!
 *
 * @author h3ll
 */
public class CacheUtilTest {


	private static final String NODE_0_VALUE = "0";
	private static final String NODE_1_VALUE = "1";

	@BeforeAll
	public static void before() {
		FSServiceConfig config = null;
		try {
			config = new FSServiceConfig(SessionDistributorServiceConfig.getInstance().getSdSessionsFSRootFolder(), SessionDistributorServiceConfig.getInstance().getSdSessionsFileExtension());
			FSService<SDCache> fsPersistence = FSServiceFactory.createFSService(config);
			//remove stored cache fro 0 instance
			fsPersistence.delete(new FSSaveableID(NODE_0_VALUE, NODE_0_VALUE));
			//remove stored cache fro 1 instance
			fsPersistence.delete(new FSSaveableID(NODE_1_VALUE, NODE_1_VALUE));
			//remove stored cache fro DEFAULT instance
			fsPersistence.delete(new FSSaveableID("1000", "1000"));
		} catch (FSServiceConfigException e) {
			Assertions.fail("Should not happen!" + e.getMessage());
		} catch (FSServiceException e) {
			Assertions.fail("Should not happen!" + e.getMessage());
		}

	}

	@BeforeEach
	public void beforeM(){
		before();
	}

	@AfterAll
	public static void after() {
		before();
	}

	@Test
	public void testFlow() {
		//prevents ooE
		System.setProperty("JUNITTEST", String.valueOf(true));
		//creating some cache!
		SDCache cache = SDCacheUtil.createCache();
		Assertions.assertNotNull(cache, "Is null");


		String id = cache.createSession("123123qweqweqweqweqweqeasflk");
		Assertions.assertNotNull(id, "is null");

		try {
			DistributedSessionVO session = cache.getSession(id);
			Assertions.assertNotNull(session, "Is null");
			Assertions.assertEquals(session.getName(), id, " Not equals");


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
			Assertions.assertNotNull(session2, "Is null");
			Assertions.assertEquals(session2.getName(), session.getName(), " Not properly restored!");

			String id5 = cache.createSession("123123qweqweqweqweqweqeasflkqweqweq");
			cache.updateCallTime(id5);

			Assertions.assertNotNull(id5, "is null");

			//remove all stuff!
			cache.removeSession(id);


			cache.removeSession(id5);

			SessionDistributorServiceConfig.getInstance().setMultipleInstancesEnabled(false);
			//try to read not existing session
			try {
				cache.getSession(id);
				Assertions.fail("Already deleted");
			} catch (NoSuchDistributedSessionException e) {
			}


		} catch (NoSuchDistributedSessionException e) {
			Assertions.fail("can't happen!!!");
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
		Assertions.assertNotNull(cacheInstance1, "Is null");

		//Setting proper  Node ID  via system property!  before cache creation  --  1 id for this node
		System.setProperty(SessionDistributorServiceConfig.getInstance().getNodeIdSystemPropertyName(), NODE_1_VALUE);
		SDCache cacheInstance2 = SDCacheUtil.createCache();
		Assertions.assertNotNull(cacheInstance2, "Is null");

		String id1 = cacheInstance1.createSession(sessionId1);
		Assertions.assertEquals(id1, sessionId1);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			Assertions.fail();
		}
		String id2 = cacheInstance1.createSession(sessionId2);
		Assertions.assertEquals(id2, sessionId2);

		//try to get first session from secondCache
		try {
			DistributedSessionVO session1 = cacheInstance2.getSession(sessionId1);
		} catch (NoSuchDistributedSessionException e) {
			Assertions.fail("Should not happen! Replication mistMatch!!!");
		}
		//try to get first session from firstCache!
		try {
			DistributedSessionVO session2 = cacheInstance1.getSession(sessionId2);
		} catch (NoSuchDistributedSessionException e) {
			Assertions.fail("Should not happen! Replication mistMatch!!!");
		}
		Assertions.assertEquals(cacheInstance1.getCount(), cacheInstance2.getCount(), "Error");
		Assertions.assertEquals(cacheInstance1.getSessions(), cacheInstance2.getSessions(), "Error");


		// adding some attribute!!!!!
		final String attributeName = "att1";
		try {
			cacheInstance1.addAttribute(sessionId1, new DistributedSessionAttribute(attributeName, new byte[0]));
			DistributedSessionVO session = cacheInstance1.getSession(sessionId1);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				Assertions.fail();
			}

			DistributedSessionVO session1 = cacheInstance2.getSession(sessionId1);
			Assertions.assertTrue(session1.getDistributedAttributes().containsKey(attributeName), "Replication error! Attribute not present!!");
			Assertions.assertEquals(session.getLastChangeTime(), session1.getLastChangeTime(), "Last change time differs!");


		} catch (NoSuchDistributedSessionException e) {
			Assertions.fail();
		}

		// set UserID
		final String userID = "userID";
		final String editorId = "editorId";
		try {
			cacheInstance1.updateSessionUserId(sessionId1, userID);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				Assertions.fail();
			}
			cacheInstance1.updateSessionEditorId(sessionId1, editorId);

			DistributedSessionVO session = cacheInstance1.getSession(sessionId1);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				Assertions.fail();
			}

			DistributedSessionVO session1 = cacheInstance2.getSession(sessionId1);
			Assertions.assertEquals(session1.getEditorId(), editorId, "Replication error! editor id not p[resent!!");
			Assertions.assertEquals(session1.getUserId(), userID, "Replication error! user id not p[resent!!");
			Assertions.assertEquals(session.getLastChangeTime(), session1.getLastChangeTime(), "Last change time differs!");


		} catch (NoSuchDistributedSessionException e) {
			Assertions.fail();
		}


		//Keep allive!
		try {
			cacheInstance1.updateCallTime(sessionId1);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				Assertions.fail();
			}
			DistributedSessionVO session = cacheInstance1.getSession(sessionId1);
			DistributedSessionVO session1 = cacheInstance2.getSession(sessionId1);
			Assertions.assertEquals(session.getLastChangeTime(), session1.getLastChangeTime(), "Last change time differs!");


		} catch (NoSuchDistributedSessionException e) {
			Assertions.fail();
		}

		//Delete calls!
		try {
			cacheInstance1.removeSession(sessionId1);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				Assertions.fail();
			}
			try {
				cacheInstance2.getSession(sessionId1);
				Assertions.fail("Error! Was not deleted by Async call! - Cache2 session1");
			} catch (NoSuchDistributedSessionException e) {
			}
			cacheInstance2.removeSession(sessionId2);
			try {
				cacheInstance1.getSession(sessionId2);
				Assertions.fail("Error! Was not deleted by Async call!  Cache1 session2");
			} catch (NoSuchDistributedSessionException e) {
			}
			Assertions.assertEquals(cacheInstance1.getCount(), 0, "Error! smth present");
			Assertions.assertEquals(cacheInstance2.getCount(), 0, "Error! smth present");
		} catch (NoSuchDistributedSessionException e) {
			Assertions.fail();
		}
		SessionDistributorServiceConfig.getInstance().setMultipleInstancesEnabled(false);

	}


}
