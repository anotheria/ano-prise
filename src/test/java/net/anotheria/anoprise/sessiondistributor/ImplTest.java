package net.anotheria.anoprise.sessiondistributor;

import net.anotheria.anoprise.metafactory.Extension;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.anoprise.metafactory.ServiceFactory;
import net.anotheria.util.IdCodeGenerator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class ImplTest {

	private SessionDistributorService service;

	private final String testSessionId = "123123123";

	@Before
	public void init() {
		MetaFactory.addAlias(SessionDistributorService.class, Extension.LOCAL);
		MetaFactory.addFactoryClass(SessionDistributorService.class, Extension.LOCAL, SDFactory.class);
	}

	@After
	public void deInit() {
		MetaFactory.reset();
	}

	@Test
	public void testCreateAndRestore() throws SessionDistributorServiceException {
		String name = "bla";
		try {
			try {
				service = MetaFactory.get(SessionDistributorService.class);
				service.restoreDistributedSession(name, "");
				fail("exception expected");
			} catch (NoSuchDistributedSessionException e) {
			}


			name = service.createDistributedSession(testSessionId);
			assertEquals("Should be same id!", name, testSessionId);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				//ignore
			}
			name = service.createDistributedSession(testSessionId);
			assertTrue("Ids should not match!!!", !name.equals(testSessionId));

			DistributedSessionVO session1 = service.restoreDistributedSession(testSessionId, "");
			DistributedSessionVO session2 = service.restoreDistributedSession(name, "");
			assertNotSame("Should not be same!", session1, session2);
			assertFalse(session1.getLastChangeTime() == session2.getLastChangeTime());
			assertFalse(session1.equals(session2));
		} catch (MetaFactoryException e) {
			Assert.fail();
		}

	}

	@Test
	public void testAddAttribute() throws SessionDistributorServiceException {
		try {
			service = MetaFactory.get(SessionDistributorService.class);
		} catch (MetaFactoryException e) {
			throw new RuntimeException(e);
		}

		List<DistributedSessionAttribute> attributesDummy = createDummy();

		String name = service.createDistributedSession(testSessionId);
		long sessionCreateTime = 0;
		service.restoreDistributedSession(name, "").setLastChangeTime(sessionCreateTime);

		List<DistributedSessionAttribute> dummy1 = createDummy();
		for (DistributedSessionAttribute attribute : dummy1) {
			service.addDistributedAttribute(name, attribute);
			assertTrue(service.restoreDistributedSession(name, "").getLastChangeTime() > sessionCreateTime);
		}
		for (DistributedSessionAttribute attribute : attributesDummy) {
			assertTrue("Should contains such attribute!!!", service.restoreDistributedSession(name, "").getDistributedAttributes().containsValue(attribute));
			assertTrue("Should contains such attribute!!!", service.restoreDistributedSession(name, "").getDistributedAttributes().containsKey(attribute.getName
					()));
		}


		List<DistributedSessionAttribute> attributesDummy2 = createDummy();
		service.addDistributedAttribute(name, attributesDummy2.get(0));
		service.addDistributedAttribute(name, attributesDummy2.get(1));


		for (DistributedSessionAttribute attribute : attributesDummy2) {
			assertTrue("Should contains such attribute!!!", service.restoreDistributedSession(name, "").getDistributedAttributes().containsValue(attribute));
			assertTrue("Should contains such attribute!!!", service.restoreDistributedSession(name, "").getDistributedAttributes().containsKey(attribute.getName
					()));
		}


		try {
			service.addDistributedAttribute("foo", attributesDummy.get(0));
			fail("expected exception updating non existing session");
		} catch (NoSuchDistributedSessionException expected) {
		}

	}


	@Test
	public void removeAttribute() throws SessionDistributorServiceException {
		try {
			service = MetaFactory.get(SessionDistributorService.class);
		} catch (MetaFactoryException e) {
			throw new RuntimeException(e);
		}
		List<DistributedSessionAttribute> attributesDummy = createDummy();

		String name = service.createDistributedSession(testSessionId);
		long sessionCreateTime = 0;
		service.restoreDistributedSession(name, "").setLastChangeTime(sessionCreateTime);

		List<DistributedSessionAttribute> dummy1 = createDummy();
		long lastChangeTime = 0;
		for (DistributedSessionAttribute attribute : dummy1) {
			service.addDistributedAttribute(name, attribute);
			assertTrue(service.restoreDistributedSession(name, "").getLastChangeTime() > sessionCreateTime);
		}
		assertEquals(dummy1.size(), service.restoreDistributedSession(name, "").getDistributedAttributes().size());
		for (DistributedSessionAttribute attribute : attributesDummy) {
			lastChangeTime = service.restoreDistributedSession(name, "").getLastChangeTime();

			service.removeDistributedAttribute(name, attribute.getName());
		}
		assertTrue(lastChangeTime > sessionCreateTime);
		assertEquals(0, service.restoreDistributedSession(name, "").getDistributedAttributes().size());


		try {
			service.removeDistributedAttribute("123123", "");
			Assert.fail("Session does not exist");
		} catch (NoSuchDistributedSessionException exception) {
		}

	}

	@Test
	public void testDelete() throws SessionDistributorServiceException {
		try {
			service = MetaFactory.get(SessionDistributorService.class);
		} catch (MetaFactoryException e) {
			throw new RuntimeException(e);
		}
		try {
			service.deleteDistributedSession("foo");
			fail("expected exception");
		} catch (NoSuchDistributedSessionException expected) {
		}


		String name1 = service.createDistributedSession(testSessionId);
		String name2 = service.createDistributedSession(testSessionId);
		assertFalse(name1.equals(name2));


		try {
			service.deleteDistributedSession(name1);
			service.restoreDistributedSession(name1, "");
		} catch (NoSuchDistributedSessionException expected) {
		}

		service.deleteDistributedSession(name2);
		try {
			service.deleteDistributedSession(name2);
		} catch (NoSuchDistributedSessionException expected) {
		}

		assertTrue(service.getDistributedSessionNames().size() == 0);
	}

	@Test
	public void testNames() throws SessionDistributorServiceException {
		try {
			service = MetaFactory.get(SessionDistributorService.class);
		} catch (MetaFactoryException e) {
			throw new RuntimeException(e);
		}
		List<String> names = new ArrayList<String>();
		assertEquals(0, service.getDistributedSessionNames().size());

		for (int i = 0; i < 10; i++)
			names.add(service.createDistributedSession(testSessionId));

		assertEquals(10, service.getDistributedSessionNames().size());

		HashSet<String> namesSet = new HashSet<String>();
		namesSet.addAll(service.getDistributedSessionNames());
		assertEquals(10, namesSet.size());

		for (int i = 0; i < 10; i++)
			namesSet.remove(names.get(i));

		assertTrue(namesSet.isEmpty());

		for (int i = 0; i < 10; i++)
			service.deleteDistributedSession(names.get(i));
		assertEquals(0, service.getDistributedSessionNames().size());
	}

	private List<DistributedSessionAttribute> createDummy() {
		ArrayList<DistributedSessionAttribute> attributeDistributeds = new ArrayList<DistributedSessionAttribute>();

		attributeDistributeds.add(new DistributedSessionAttribute("a1", null));
		attributeDistributeds.add(new DistributedSessionAttribute("a2", new byte[]{1, 2, 3}));

		return attributeDistributeds;
	}


	@Test
	public void testWrightToFsFirst() {
		//create sessions ids!
		SessionDistributorServiceConfig.getInstance().setWrightSessionsToFsOnShutdownEnabled(true);
		try {
			MetaFactory.addAlias(SessionDistributorService.class, Extension.LOCAL);
			MetaFactory.addFactoryClass(SessionDistributorService.class, Extension.LOCAL, SDFactory.class);
			SessionDistributorService sdService = MetaFactory.get(SessionDistributorService.class);

			if (sdService.getDistributedSessionNames().size() == 0)
				//creating sessions!!
				for (int i = 0; i < 15; i++)
					try {
						sdService.createDistributedSession("test" + i);
					} catch (SessionDistributorServiceException e) {
						Assert.fail("Should not happen");
					}


			else {


				SessionDistributorServiceConfig.getInstance().setWrightSessionsToFsOnShutdownEnabled(false);
				Assert.assertEquals("Should be  same size!!!", 15, sdService.getDistributedSessionNames().size());

			}
		} catch (SessionDistributorServiceException e) {
			Assert.fail("Should not happen!");
		} catch (Throwable throwable) {
		}
	}

	@Test
	public void testWrightToFsSecond() {
		//create sessions ids!
		SessionDistributorServiceConfig.getInstance().setWrightSessionsToFsOnShutdownEnabled(true);
		try {
			MetaFactory.addAlias(SessionDistributorService.class, Extension.LOCAL);
			MetaFactory.addFactoryClass(SessionDistributorService.class, Extension.LOCAL, SDFactory.class);
			SessionDistributorService sdService = MetaFactory.get(SessionDistributorService.class);

			if (sdService.getDistributedSessionNames().size() == 0)
				//creating sessions!!
				for (int i = 0; i < 15; i++)
					try {
						sdService.createDistributedSession("test" + i);
					} catch (SessionDistributorServiceException e) {
						Assert.fail("Should not happen");
					}


			else {


				SessionDistributorServiceConfig.getInstance().setWrightSessionsToFsOnShutdownEnabled(false);
				Assert.assertEquals("Should be  same size!!!", 15, sdService.getDistributedSessionNames().size());

			}
		} catch (SessionDistributorServiceException e) {
			Assert.fail("Should not happen!");
		} catch (Throwable throwable) {
		}
	}


	@Test
	public void testCreationWithLimiting() {
		final int sessionsLimit = 2;
		MetaFactory.reset();
		try {
			MetaFactory.addAlias(SessionDistributorService.class, Extension.LOCAL);
			MetaFactory.addFactoryClass(SessionDistributorService.class, Extension.LOCAL, SDFactory.class);

			final SessionDistributorService sdService = MetaFactory.get(SessionDistributorService.class);

			SessionDistributorServiceConfig.getInstance().setMaxSessionsCount(sessionsLimit);
			SessionDistributorServiceConfig.getInstance().setSessionsLimitEnabled(true);


			final AtomicInteger failedCallsAmount = new AtomicInteger(0);
			final int nThreads = 100;
			final CountDownLatch prepareLatch = new CountDownLatch(nThreads);
			final CountDownLatch startLatch = new CountDownLatch(1);
			final CountDownLatch stopLatch = new CountDownLatch(nThreads);


			for (int i = 0; i < nThreads; i++) {
				Thread t = new Thread() {

					@Override
					public void run() {
						try {
							prepareLatch.countDown();
							startLatch.await();

							try {
								sdService.createDistributedSession(IdCodeGenerator.generateCode(15));
								if (failedCallsAmount.get() == sessionsLimit)
									Assert.fail("Limit Reached - But Exception does not  fall!");

							} catch (SessionsCountLimitReachedSessionDistributorServiceException e) {
								failedCallsAmount.incrementAndGet();
							} catch (SessionDistributorServiceException e) {
								Assert.fail(e.getMessage());
							}


						} catch (InterruptedException e) {
							Assert.fail(e.getMessage());
						} finally {
							stopLatch.countDown();
						}
					}
				};
				t.start();
			}

			try {
				prepareLatch.await();
				startLatch.countDown();

				stopLatch.await();
			} catch (InterruptedException e) {
				Assert.fail(e.getMessage());
			}


			Assert.assertEquals("Should be EQUALS", nThreads - sessionsLimit, failedCallsAmount.get());
		} catch (MetaFactoryException e) {
			Assert.fail();
		}

		// Setting to prev  values!
		SessionDistributorServiceConfig.getInstance().setMaxSessionsCount(1000);
		SessionDistributorServiceConfig.getInstance().setSessionsLimitEnabled(false);
	}

	/**
	 * Factory for test!
	 */
	public static class SDFactory implements ServiceFactory<SessionDistributorService> {
		@Override
		public SessionDistributorService create() {

			return new SessionDistributorServiceImpl();
		}
	}
}
