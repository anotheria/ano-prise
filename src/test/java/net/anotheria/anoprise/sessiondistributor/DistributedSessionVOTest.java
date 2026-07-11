package net.anotheria.anoprise.sessiondistributor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Represent test for simple bean.
 */
public class DistributedSessionVOTest {
	@Test
	public void testExpiry() throws Exception {
		DistributedSessionVO h = new DistributedSessionVO(null);
		SessionDistributorServiceConfig config = SessionDistributorServiceConfig.getInstance();
		config.setDistributedSessionMaxAge(100);
		assertFalse(h.isExpired(), "Should not be expired, cause just created");
		Thread.sleep(200);
		assertTrue(h.isExpired(), "Should expire! cause 100ms is expiration time");

		config.setDistributedSessionMaxAge(10000);
		assertFalse(h.isExpired(), "Should not be expired, cause expiry time was increased");
	}
}
