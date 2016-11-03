package net.anotheria.anoprise.dualcrud;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class TestDual {
	
	private static CrudService<TestCrudsaveable> alpha = new TestCrudServiceAlpha();
	private static CrudService<TestCrudsaveable> beta  = new TestCrudServiceBeta();
	
	@Test public void testOnTheFlyMigration() throws Exception{
		
		DualCrudService<TestCrudsaveable> testService = DualCrudServiceFactory.createDualCrudService(alpha, beta, DualCrudConfig.migrateOnTheFly());
		
		String id = "onthefly";
		String content = "on-the-fly-content";
		
		TestCrudsaveable a = new TestCrudsaveable(id, content);
		
		alpha.delete(a);
		beta.delete(a);
		
		//start testing
		//test create -> create creates on the new branch.
		testService.create(a);
		assertTrue(testService.exists(a));
		assertTrue(beta.exists(a));
		assertFalse(alpha.exists(a));
		assertEquals(testService.read(new SaveableID(a.getId(), a.getId())), a);


		//test on the fly migration
		alpha.delete(a);
		beta.delete(a);
		alpha.create(a);
		assertTrue(testService.exists(a));
		assertFalse(beta.exists(a));
		assertTrue(alpha.exists(a));

		//now read and force migration.
		assertEquals(testService.read(new SaveableID(a.getId(), a.getId())), a);
		assertTrue(testService.exists(a));
		assertTrue("instance should have been moved to beta", beta.exists(a));
		assertFalse("instance should have been moved to beta", alpha.exists(a));


		//test delete
		testService.delete(a);
		assertFalse("instance should be deleted on new", beta.exists(a));
		assertFalse("instance should be deleted on old", alpha.exists(a));

		//test migration on update
		TestCrudsaveable b = new TestCrudsaveable(id, "bla"+content);
		alpha.create(a);
		testService.update(b);
		assertTrue(testService.exists(a));
		assertTrue("instance should have been moved to beta", beta.exists(a));
		assertFalse("instance should have been moved to beta", alpha.exists(a));

		//test migrate
		testService.delete(a);
		assertFalse("instance should be deleted on new", beta.exists(a));
		assertFalse("instance should be deleted on old", alpha.exists(a));
		alpha.create(a);
		testService.migrate(new SaveableID(a.getOwnerId(), a.getOwnerId()));
		assertTrue("instance should exists deleted on new", beta.exists(a));
		assertFalse("instance should be deleted on old", alpha.exists(a));

		//test save on the fly.
		testService.delete(a);
		assertFalse("instance should be deleted on new", beta.exists(a));
		assertFalse("instance should be deleted on old", alpha.exists(a));
		alpha.create(a);
		testService.save(a);
		assertTrue("instance should exists deleted on new", beta.exists(a));
		assertFalse("instance should be deleted on old", alpha.exists(a));


	}
	//this tests tests how the dualServices handles illegal incorrect situations.

	@Test public void testOnTheFlyMigrationAutofix() throws Exception{

		DualCrudService<TestCrudsaveable> testService = DualCrudServiceFactory.createDualCrudService(alpha, beta, DualCrudConfig.migrateOnTheFly());

		String id = "ontheflyautofix";
		String content = "on-the-fly-contentautofix";

		TestCrudsaveable a = new TestCrudsaveable(id, content);
		TestCrudsaveable b = new TestCrudsaveable(id, content+"foo");

		alpha.delete(a);
		beta.delete(a);

		alpha.create(a);
		beta.create(a);

		//this is wrong, there should be no stuation where a file exists on both!
		testService.save(a);
		assertTrue("instance should exists deleted on new", beta.exists(a));
		assertFalse("instance should be deleted on old", alpha.exists(a));

		alpha.create(a);
		testService.update(b);
		assertTrue("instance should exists deleted on new", beta.exists(a));
		assertFalse("instance should be deleted on old", alpha.exists(a));

		assertFalse(a.equals(testService.read(new SaveableID(a.getOwnerId(), a.getOwnerId()))));


	}

	@Test public void testSingleModeLeftOnly() throws Exception{
		DualCrudService<TestCrudsaveable> testService = DualCrudServiceFactory.createDualCrudService(alpha, beta, DualCrudConfig.useLeftOnly());
		testSingleMode(testService, alpha, beta);
	}

	@Test public void testSingleModeRightOnly() throws Exception{
		DualCrudService<TestCrudsaveable> testService = DualCrudServiceFactory.createDualCrudService(alpha, beta, DualCrudConfig.useRightOnly());
		testSingleMode(testService, beta, alpha);
	}

	private void testSingleMode(DualCrudService<TestCrudsaveable> dualService, CrudService<TestCrudsaveable> usedService, CrudService<TestCrudsaveable> unusedService) throws Exception{

		String id = "singlemode";
		String content = "singlemode";

		TestCrudsaveable a = new TestCrudsaveable(id, content);

		//cleanup
		alpha.delete(a);
		beta.delete(a);

		//start testing
		//test create -> create creates on the new branch.
		dualService.create(a);
		assertTrue(dualService.exists(a));
		assertTrue(usedService.exists(a));
		assertFalse(unusedService.exists(a));
		assertEquals(dualService.read(new SaveableID(a.getId(), a.getId())), a);


		//now read and check that no migration happens migration.
		assertEquals(dualService.read(new SaveableID(a.getId(), a.getId())), a);
		assertTrue(dualService.exists(a));
		assertTrue(usedService.exists(a));
		assertFalse(unusedService.exists(a));


		//test delete
		dualService.delete(a);
		assertFalse(usedService.exists(a));
		assertFalse(unusedService.exists(a));


		TestCrudsaveable b = new TestCrudsaveable(id, content+"foo");
		try{
			dualService.update(b);
			fail("expected exception");
		}catch(CrudServiceException e){}

		dualService.save(a);
		assertTrue(dualService.exists(a));
		assertTrue(usedService.exists(a));
		assertFalse(unusedService.exists(a));
		assertEquals(dualService.read(new SaveableID(a.getId(), a.getId())), a);

		dualService.update(b);
		assertTrue(dualService.exists(a));
		assertTrue(usedService.exists(a));
		assertFalse(unusedService.exists(a));
		assertFalse(dualService.read(new SaveableID(a.getId(), a.getId())).equals(a));

		try{
			dualService.migrate(new SaveableID(a.getOwnerId(), a.getOwnerId()));
			fail("migrate should throw error in single mode.");
		}catch(CrudServiceException ignored){}



	}

	@Test public void testDuplicate() throws Exception{
		DualCrudService<TestCrudsaveable> dualService = DualCrudServiceFactory.createDualCrudService(alpha, beta, DualCrudConfig.duplicate());

		String id = "duplicate";
		String content = "duplicate-content";

		TestCrudsaveable a = new TestCrudsaveable(id, content);

		//cleanup
		alpha.delete(a);
		beta.delete(a);

		//start testing
		//test create -> create creates on the new branch.
		dualService.create(a);
		assertTrue(dualService.exists(a));
		assertTrue(alpha.exists(a));
		assertTrue(beta.exists(a));
		assertEquals(dualService.read(new SaveableID(a.getId(), a.getId())), a);


		//now read and check that no migration happens migration.
		assertEquals(dualService.read(new SaveableID(a.getId(), a.getId())), a);
		assertTrue(dualService.exists(a));
		assertTrue(alpha.exists(a));
		assertTrue(beta.exists(a));


		//test delete
		dualService.delete(a);
		assertFalse(alpha.exists(a));
		assertFalse(beta.exists(a));


		TestCrudsaveable b = new TestCrudsaveable(id, content+"foo");
		try{
			dualService.update(b);
			fail("expected exception");
		}catch(CrudServiceException e){}

		dualService.save(a);
		assertTrue(dualService.exists(a));
		assertTrue(alpha.exists(a));
		assertTrue(beta.exists(a));
		assertEquals(dualService.read(new SaveableID(a.getId(), a.getId())), a);

		dualService.update(b);
		assertTrue(dualService.exists(a));
		assertTrue(alpha.exists(a));
		assertTrue(beta.exists(a));
		assertFalse(dualService.read(new SaveableID(a.getId(), a.getId())).equals(a));
		
		
	}
}
