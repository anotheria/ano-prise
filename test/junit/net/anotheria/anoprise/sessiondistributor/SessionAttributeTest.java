package net.anotheria.anoprise.sessiondistributor;

import static org.junit.Assert.*;

import org.junit.Test;

public class SessionAttributeTest {
	@Test public void testToString(){
		assertNotNull(new SessionAttribute("", null).toString());
		assertNotNull(new SessionAttribute("bla", null).toString());
		assertNotNull(new SessionAttribute("bla", new byte[]{1,2,3}).toString());
	}
}
