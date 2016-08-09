package net.anotheria.anoprise.inmemorymirror;

import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class InMemoryMirrorTest {

	@Test public void testInMemoryMirror()  throws InMemoryMirrorException{
		InMemoryMirror< String, TestData> mirror = InMemoryMirrorFactory.createMirror(new MySupport());

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

	@Test public void testCreateLocalOnly()  throws InMemoryMirrorException{
		MySupport support = new MySupport();
		InMemoryMirror<String, TestData> mirror = InMemoryMirrorFactory.createMirror(support);

		assertEquals(100, mirror.getAll().size());

		TestData newD = new TestData("foo");
		TestData created = mirror.createLocalOnly(newD);
		String key = created.getKey();

		assertEquals(101, mirror.getAll().size());
		assertEquals("Should be created in the mirror", "foo", mirror.get(key).getValue());
		assertNull("Should be not created in the backend/support", support.getForDebug(key));
	}

	@Test public void testDelete()  throws InMemoryMirrorException{
		InMemoryMirror< String, TestData> mirror = InMemoryMirrorFactory.createMirror(new MySupport());

		assertEquals(100, mirror.getAll().size());
		for (int i=0; i<100; i++){
			String id = String.valueOf(i);
			TestData firstCallResult = mirror.remove(id);
			TestData secondCallResult = mirror.remove(id);

			assertNull(secondCallResult);
			assertNotNull(firstCallResult);
			assertEquals(firstCallResult.getKey(), id);
			assertEquals(firstCallResult.getValue(), "val"+id);
		}
	}

	@Test public void testDeleteLocalOnly()  throws InMemoryMirrorException{
		MySupport support = new MySupport();
		InMemoryMirror<String, TestData> mirror = InMemoryMirrorFactory.createMirror(support);

		assertEquals(100, mirror.getAll().size());
		for (int i = 0; i < 100; i++) {
			String id = String.valueOf(i);
			TestData result = mirror.removeLocalOnly(id);
			assertEquals(id, result.getKey());
			try {
				mirror.get(id);
				fail("Exception expected on getting element: " + id);
			} catch (ElementNotFoundException e) {
				// expected
			}
			assertNotNull("Should still present in backend/support", support.getForDebug(id));
		}
	}

	@Test public void testUpdate() throws InMemoryMirrorException{
		MySupport support = new MySupport();
		InMemoryMirror< String, TestData> mirror = InMemoryMirrorFactory.createMirror(support);

		assertEquals(100, mirror.getAll().size());
		for (int i=0; i<100; i++){
			String id = String.valueOf(i);
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

	@Test public void testUpdateLocalOnly() throws InMemoryMirrorException{
		MySupport support = new MySupport();
		InMemoryMirror<String, TestData> mirror = InMemoryMirrorFactory.createMirror(support);

		assertEquals(100, mirror.getAll().size());
		for (int i = 0; i < 100; i++) {
			String id = String.valueOf(i);
			TestData oldData = mirror.get(id);
			TestData newData = oldData.clone();//make a copy to ensure that the object is really new.


			assertEquals(oldData.getValue(), newData.getValue());
			//check backend
			assertEquals(oldData.getValue(), support.getForDebug(id).getValue());
			newData.setValue("newval" + id);

			mirror.updateLocalOnly(newData);

			//check backend
			assertEquals("Mirror value should be canged", newData.getValue(), mirror.get(id).getValue());
			assertEquals("Backend/support value should be not changed", oldData.getValue(), support.getForDebug(id).getValue());
		}
	}

	@Test public void testNegativeBehaviour() throws InMemoryMirrorException{
		MySupport support = new MySupport();
		InMemoryMirror< String, TestData> mirror = InMemoryMirrorFactory.createMirror(support);

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

		private Map<String, TestData> data = new HashMap<>();
		private AtomicLong id = new AtomicLong(0);
		public MySupport(){
			for (int i=0; i<100; i++){
				data.put(String.valueOf(i), new TestData(String.valueOf(i), "val"+i));
			}
			id.set(99);
		}

		@Override
		public Collection<TestData> readAll() {
			return data.values();
		}

		@Override
		public TestData create(TestData element) {
			element.setId(String.valueOf(id.incrementAndGet()));
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
