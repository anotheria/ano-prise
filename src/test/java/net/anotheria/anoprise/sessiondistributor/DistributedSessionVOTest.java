package net.anotheria.anoprise.sessiondistributor;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Represent test for simple bean.
 */
public class DistributedSessionVOTest {
	@Test
	public void testExpiry() throws Exception {
		DistributedSessionVO h = new DistributedSessionVO(null);
		SessionDistributorServiceConfig config = SessionDistributorServiceConfig.getInstance();
		config.setDistributedSessionMaxAge(100);
		assertFalse("Should not be expired, cause just created", h.isExpired());
		Thread.sleep(200);
		assertTrue("Should expire! cause 100ms is expiration time", h.isExpired());

		config.setDistributedSessionMaxAge(10000);
		assertFalse("Should not be expired, cause expiry time was increased", h.isExpired());
	}
}
