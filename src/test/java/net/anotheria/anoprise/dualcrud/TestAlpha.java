package net.anotheria.anoprise.dualcrud;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestAlpha {
	@Test public void simpleCrudTest() throws Exception{
		String id = "1";
		String content = "yoohei";
		TestCrudsaveable a = new TestCrudsaveable(id, content);
		
		CrudService<TestCrudsaveable> service = new TestCrudServiceAlpha();
		
		//cleanup
		service.delete(a);
		
		service.create(a);
		
		try{
			service.create(a);
			fail("create of existing object should fail.");
		}catch(CrudServiceException e){
		}
		
		TestCrudsaveable b = service.read(new SaveableID(id, id));
		assertNotNull(b);
		
		assertEquals(a.getOwnerId(), b.getOwnerId());
		assertEquals(a.getId(), b.getId());
		assertEquals(a.getContent(), b.getContent());
		assertNotSame(a, b);
		
		TestCrudsaveable c = new TestCrudsaveable(id, content+"bla");
		service.update(c);
		TestCrudsaveable d = service.read(new SaveableID(id, id));
		assertNotNull(d);
		
		assertEquals(a.getOwnerId(), d.getOwnerId());
		assertEquals(a.getId(), d.getId());
		assertFalse(a.getContent().equals(d.getContent()));
		assertNotSame(a, d);
		
		service.delete(d);
		try{
			service.update(c);
			fail("updating non existing object should fail");
		}catch(Exception e){}
		
		service.save(c);
		
		
		
	}
}
