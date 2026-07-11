package net.anotheria.anoprise.dualcrud;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

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
		assertTrue(beta.exists(a), "instance should have been copied to beta");
		assertTrue(alpha.exists(a), "instance should have been keeped in alpha");
		
		
		//test delete
		testService.delete(a);
		assertFalse(beta.exists(a), "instance should be deleted on new");
		assertFalse(alpha.exists(a), "instance should be deleted on old");
		
		//test migration on update
		TestCrudsaveable b = new TestCrudsaveable(id, "bla"+content);
		alpha.create(a);
		testService.save(b);
		assertTrue(testService.exists(a));
		assertTrue(beta.exists(a), "instance should have been copied to beta");
		assertTrue(alpha.exists(a), "instance should have been keeped in alpha");

		//test migrate
		testService.delete(a);
		assertFalse(beta.exists(a), "instance should be deleted on new");
		assertFalse(alpha.exists(a), "instance should be deleted on old");
		alpha.create(a);
		testService.migrate(new SaveableID(a.getOwnerId(), a.getOwnerId()));
		assertTrue(beta.exists(a), "instance should exists deleted on new");
		assertFalse(alpha.exists(a), "instance should be deleted on old");

		//test save on the fly.
		testService.delete(a);
		assertFalse(beta.exists(a), "instance should be deleted on new");
		assertFalse(alpha.exists(a), "instance should be deleted on old");
		alpha.create(a);
		testService.save(a);
		assertTrue(beta.exists(a), "instance should exists on new");
		assertTrue(alpha.exists(a), "instance should exists in old too");
		
		
	}
}
