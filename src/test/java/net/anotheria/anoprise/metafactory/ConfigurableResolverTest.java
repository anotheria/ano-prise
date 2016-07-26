package net.anotheria.anoprise.metafactory;

import org.configureme.ConfigurationManager;
import org.configureme.environments.DynamicEnvironment;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConfigurableResolverTest {
	
	private static ConfigurableResolver resolver;

	@BeforeClass public static void setConfigureMe(){
		ConfigurationManager.INSTANCE.setDefaultEnvironment(new DynamicEnvironment("test", "junit"));
	}

	@BeforeClass public static void setUp(){
		resolver = ConfigurableResolver.create();
	}
	
	@Test public void resolveAliasTest(){
		assertEquals("foo.bar.XxxService", resolver.resolveAlias("XxxService"));
		assertEquals("XxxService", resolver.resolveAlias("DomainXxxService"));
		assertEquals("XxxService", resolver.resolveAlias("CmsXxxService"));
		assertNull(resolver.resolveAlias("UknownXxxService"));
	}
}
