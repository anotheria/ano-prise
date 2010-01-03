package net.anotheria.anoprise.sessiondistributor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ImplTest {
	
	private SessionDistributorService service;
	
	@Before public void init(){
		service = new SessionDistributorServiceImpl();
	}
	
	@Test public void testGet() throws SessionDistributorServiceException{
		String name = "bla";
		try{
			service.getDistributedSession(name);
			fail("exception expected");
		}catch(NoSuchDistributedSessionException e){}
		
		List<SessionAttribute> attributesDummy = createDummy();
		name = service.createDistributedSession(attributesDummy);
		List<SessionAttribute> fromService = service.getDistributedSession(name);
		assertEquals(attributesDummy, fromService);
		assertSame(attributesDummy, fromService);
		
		
	}
	
	@Test public void testUpdate() throws SessionDistributorServiceException{
		List<SessionAttribute> attributesDummy = createDummy();
		String name = service.createDistributedSession(attributesDummy);
		List<SessionAttribute> fromService = service.getDistributedSession(name);
		assertEquals(attributesDummy, fromService);
		assertSame(attributesDummy, fromService);
		
		List<SessionAttribute> attributesDummy2 = createDummy();
		attributesDummy2.remove(0);
		service.updateDistributedSession(name, attributesDummy2);
		List<SessionAttribute> fromService2 = service.getDistributedSession(name);
		
		assertEquals(attributesDummy2, fromService2);
		assertNotSame(attributesDummy, fromService2);
		assertFalse(fromService2.equals(fromService));
		assertNotSame(fromService, fromService2);
		
		try{
			service.updateDistributedSession("foo", attributesDummy);
			fail("expected exception updating non existing session");
		}catch(NoSuchDistributedSessionException expected){}
		
	}
	
	@Test public void testDelete() throws SessionDistributorServiceException{
		List<SessionAttribute> attributesDummy = createDummy();
		try{
			service.deleteDistributedSession("foo");
			fail("expected exception");
		}catch(NoSuchDistributedSessionException expected){};
		try{
			service.getAndDeleteDistributedSession("foo");
			fail("expected exception");
		}catch(NoSuchDistributedSessionException expected){};
		
		String name1 = service.createDistributedSession(attributesDummy);
		String name2 = service.createDistributedSession(attributesDummy);
		assertFalse(name1.equals(name2));
		
		List<SessionAttribute> fromService = service.getAndDeleteDistributedSession(name1);
		assertEquals(fromService, attributesDummy);
		try{
			fromService = service.getAndDeleteDistributedSession(name1);
		}catch(NoSuchDistributedSessionException expected){}
		
		service.deleteDistributedSession(name2);
		try{
			service.deleteDistributedSession(name2);
		}catch(NoSuchDistributedSessionException expected){}
		
		assertTrue(service.getDistributedSessionNames().size()==0);
	}
	
	@Test public void testNames() throws SessionDistributorServiceException{
		List<String> names = new ArrayList<String>();
		assertEquals(0, service.getDistributedSessionNames().size());
		
		for (int i=0; i<10; i++)
			names.add(service.createDistributedSession(createDummy()));
		
		assertEquals(10, service.getDistributedSessionNames().size());
		
		HashSet<String> namesSet = new HashSet<String>();
		namesSet.addAll(service.getDistributedSessionNames());
		assertEquals(10, namesSet.size());
		
		for (int i=0; i<10; i++)
			namesSet.remove(names.get(i));
		
		assertTrue(namesSet.isEmpty());

		for (int i=0; i<10; i++)
			service.deleteDistributedSession(names.get(i));
		assertEquals(0, service.getDistributedSessionNames().size());
	}

	private List<SessionAttribute> createDummy(){
		ArrayList<SessionAttribute> attributes = new ArrayList<SessionAttribute>();
		
		attributes.add(new SessionAttribute("a1", null));
		attributes.add(new SessionAttribute("a2", new byte[]{1,2,3}));
		
		return attributes;
	}
}
