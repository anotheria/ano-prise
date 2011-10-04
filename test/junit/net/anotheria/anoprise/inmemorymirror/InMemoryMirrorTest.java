package net.anotheria.anoprise.inmemorymirror;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOnSupplier;

public class InMemoryMirrorTest {
	
	@Test public void testInMemoryMirror(){
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
		public TestData update(TestData element) {
			data.put(element.getKey(), element);
			return element;
		}

		@Override
		public TestData remove(String key) {
			return data.remove(key);
		}
		
	}
}
