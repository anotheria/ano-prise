package net.anotheria.anoprise.metafactory;

import org.configureme.ConfigurationManager;
import org.configureme.environments.DynamicEnvironment;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ConfigurableResolverTest {
	
	private static ConfigurableResolver resolver;

	@BeforeAll public static void setConfigureMe(){
		ConfigurationManager.INSTANCE.setDefaultEnvironment(new DynamicEnvironment("test", "junit"));
	}

	@BeforeAll public static void setUp(){
		resolver = ConfigurableResolver.create();
	}
	
	@Test public void resolveAliasTest(){
		assertEquals("foo.bar.XxxService", resolver.resolveAlias("XxxService"));
		assertEquals("XxxService", resolver.resolveAlias("DomainXxxService"));
		assertEquals("XxxService", resolver.resolveAlias("CmsXxxService"));
		assertNull(resolver.resolveAlias("UknownXxxService"));
	}
}
