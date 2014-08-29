package net.anotheria.anoprise.dualcrud;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MigrateButMaintainTest {
	
	private static CrudService<TestCrudsaveable> alpha = new TestCrudServiceAlpha();
	private static CrudService<TestCrudsaveable> beta  = new TestCrudServiceBeta();

	@Test public void testOnTheFlyMigrationWithMaintain() throws Exception{
		
		DualCrudService<TestCrudsaveable> testService = DualCrudServiceFactory.createDualCrudService(alpha, beta, DualCrudConfig.migrateOnTheFlyButMaintainBoth());
		
		String id = "ontheflyxxx";
		String content = "on-the-fly-contentxxx";
		
		TestCrudsaveable a = new TestCrudsaveable(id, content);
		
		alpha.delete(a);
		beta.delete(a);
		
		//start testing
		//test create -> create creates on the new branch.
		testService.create(a);
		assertTrue(testService.exists(a));
		assertTrue(beta.exists(a));
		assertTrue(alpha.exists(a));
		assertEquals(testService.read(a.getId()), a);
		
		
		//test on the fly migration
		alpha.delete(a);
		beta.delete(a);
		alpha.create(a);
		assertTrue(testService.exists(a));
		assertFalse(beta.exists(a));
		assertTrue(alpha.exists(a));
		
		//now read and force migration.
		assertEquals(testService.read(a.getId()), a);
		assertTrue(testService.exists(a));
		assertTrue("instance should have been copied to beta", beta.exists(a));
		assertTrue("instance should have been keeped in alpha", alpha.exists(a));
		
		
		//test delete
		testService.delete(a);
		assertFalse("instance should be deleted on new", beta.exists(a));
		assertFalse("instance should be deleted on old", alpha.exists(a));
		
		//test migration on update
		TestCrudsaveable b = new TestCrudsaveable(id, "bla"+content);
		alpha.create(a);
		testService.save(b);
		assertTrue(testService.exists(a));
		assertTrue("instance should have been copied to beta", beta.exists(a));
		assertTrue("instance should have been keeped in alpha", alpha.exists(a));

		//test migrate
		testService.delete(a);
		assertFalse("instance should be deleted on new", beta.exists(a));
		assertFalse("instance should be deleted on old", alpha.exists(a));
		alpha.create(a);
		testService.migrate(a.getOwnerId());
		assertTrue("instance should exists deleted on new", beta.exists(a));
		assertFalse("instance should be deleted on old", alpha.exists(a));

		//test save on the fly.
		testService.delete(a);
		assertFalse("instance should be deleted on new", beta.exists(a));
		assertFalse("instance should be deleted on old", alpha.exists(a));
		alpha.create(a);
		testService.save(a);
		assertTrue("instance should exists on new", beta.exists(a));
		assertTrue("instance should exists in old too", alpha.exists(a));
		
		
	}
}
