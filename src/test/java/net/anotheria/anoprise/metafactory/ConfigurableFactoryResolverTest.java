package net.anotheria.anoprise.metafactory;

import org.configureme.ConfigurationManager;
import org.configureme.environments.DynamicEnvironment;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ConfigurableFactoryResolverTest {

	private static ConfigurableFactoryResolver resolver;

	@BeforeAll public static void setUp(){
		ConfigurationManager.INSTANCE.setDefaultEnvironment(new DynamicEnvironment("test", "junit"));
		resolver = ConfigurableFactoryResolver.create();
	}


	@Test public void resolveAliasTest(){
		System.out.println(resolver.resolveFactory("net.anotheria.anoprise.mock.TestService"));
		assertEquals(net.anotheria.anoprise.mock.TestServiceFactory.class, resolver.resolveFactory("net.anotheria.anoprise.mock.TestService"));		
		assertNull(resolver.resolveFactory("net.anotheria.anosite.gen.user.service.IUserService"));
	}
}