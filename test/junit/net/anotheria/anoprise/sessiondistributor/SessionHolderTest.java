package net.anotheria.anoprise.sessiondistributor;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SessionHolderTest {
	@Test public void testExpiry() throws Exception{
		SessionHolder h = new SessionHolder(null, null);
		assertFalse(h.isExpiredForAge(100));
		Thread.currentThread().sleep(200);
		assertTrue(h.isExpiredForAge(100));
	}
}
