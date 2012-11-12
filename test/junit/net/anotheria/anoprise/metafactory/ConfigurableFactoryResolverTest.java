package net.anotheria.anoprise.metafactory;

import org.configureme.ConfigurationManager;
import org.configureme.environments.DynamicEnvironment;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ConfigurableFactoryResolverTest {

	private static ConfigurableFactoryResolver resolver;

	@BeforeClass public static void setUp(){
		ConfigurationManager.INSTANCE.setDefaultEnvironment(new DynamicEnvironment("test", "junit"));
		resolver = ConfigurableFactoryResolver.create();
	}


	@Test public void resolveAliasTest(){
		System.out.println(resolver.resolveFactory("net.anotheria.anoprise.mock.TestService"));
		assertEquals(net.anotheria.anoprise.mock.TestServiceFactory.class, resolver.resolveFactory("net.anotheria.anoprise.mock.TestService"));		
		assertNull(resolver.resolveFactory("net.anotheria.anosite.gen.user.service.IUserService"));
	}
}