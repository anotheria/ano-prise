package net.anotheria.anoprise.inmemorymirror;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOnSupplier;

public class InMemoryMirrorTest {
	
	@Test public void testInMemoryMirror()  throws InMemoryMirrorException{
		InMemoryMirror< String, TestData> mirror = InMemoryMirrorFactory.createMirror(null, new MySupport());
		
		assertEquals(100, mirror.getAll().size());
		
		
		TestData newD = new TestData("foo");
		TestData created = mirror.create(newD);
		String key = created.getKey();
		
		assertEquals(101, mirror.getAll().size());
		assertEquals(mirror.get(key).getValue(), "foo");
		
		created.setValue("yoohoo");
		mirror.update(created);
		assertEquals(101, mirror.getAll().size());
		assertEquals(mirror.get(key).getValue(), "yoohoo");
		
		
	}
	
	@Test public void testDelete()  throws InMemoryMirrorException{
		InMemoryMirror< String, TestData> mirror = InMemoryMirrorFactory.createMirror(null, new MySupport());
		
		assertEquals(100, mirror.getAll().size());
		for (int i=0; i<100; i++){
			String id = ""+i;
			TestData firstCallResult = mirror.remove(id);
			TestData secondCallResult = mirror.remove(id);
			
			assertNull(secondCallResult);
			assertNotNull(firstCallResult);
			assertEquals(firstCallResult.getKey(), id);
			assertEquals(firstCallResult.getValue(), "val"+id);
		}
	}
	
	@Test public void testUpdate() throws InMemoryMirrorException{
		MySupport support = new MySupport();
		InMemoryMirror< String, TestData> mirror = InMemoryMirrorFactory.createMirror(null, support);
		
		assertEquals(100, mirror.getAll().size());
		for (int i=0; i<100; i++){
			String id = ""+i;
			TestData oldData = mirror.get(id);
			TestData newData = oldData.clone();//make a copy to ensure that the object is really new. 
			
			
			assertEquals(oldData.getValue(), newData.getValue());
			//check backend
			assertEquals(oldData.getValue(), support.getForDebug(id).getValue());
			newData.setValue("newval"+id);
			assertFalse(oldData.getValue().equals(newData.getValue()));
			
			mirror.update(newData);
			
			//check backend
			assertEquals(newData.getValue(), support.getForDebug(id).getValue());
			assertFalse(oldData.getValue().equals(support.getForDebug(id).getValue()));
			
			
		}
	}
	
	@Test public void testNegativeBehaviour() throws InMemoryMirrorException{
		MySupport support = new MySupport();
		InMemoryMirror< String, TestData> mirror = InMemoryMirrorFactory.createMirror(null, support);

		try{
			mirror.get("foo");
			fail("exception expected");
		}catch(ElementNotFoundException e){}
		
		TestData notExistent = new TestData("x","x");
		try{
			mirror.update(notExistent);
			fail("exception expected");
		}catch(ElementNotFoundException e){}
		
	}

	static class MySupport implements InMemorySupport<String, TestData>{

		private Map<String, TestData> data = new HashMap<String, TestData>();
		private AtomicLong id = new AtomicLong(0);
		public MySupport(){
			for (int i=0; i<100; i++){
				data.put(""+i, new TestData(""+i, "val"+i));
			}
			id.set(99);
		}
		
		@Override
		public Collection<TestData> readAll() {
			return data.values();
		}

		@Override
		public TestData create(TestData element) {
			element.setId(""+id.incrementAndGet());
			data.put(element.getKey(), element);
			return element;
		}

		@Override
		public TestData update(TestData element)  throws ElementNotFoundException{
			TestData old = data.get(element.getKey());
			if (old==null)
				throw new ElementNotFoundException(element.getKey());
			data.put(element.getKey(), element);
			return element;
		}

		@Override
		public TestData remove(String key) {
			return data.remove(key);
		}
		
		TestData getForDebug(String key){
			return data.get(key);
		}
		
	}
}
