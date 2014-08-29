package net.anotheria.anoprise.sessiondistributor;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SessionAttributeTest {
	@Test
	public void testToString() {
		assertNotNull(new DistributedSessionAttribute("",  null).toString());

		DistributedSessionAttribute attr1 = new DistributedSessionAttribute("bla",  null);
		DistributedSessionAttribute attr2 = new DistributedSessionAttribute("bla",  new byte[]{1, 2, 3});
		assertNotNull(attr1.toString());
		assertNotNull(attr2.toString());
		assertEquals("Should be same", attr1, attr2);

	}
}
