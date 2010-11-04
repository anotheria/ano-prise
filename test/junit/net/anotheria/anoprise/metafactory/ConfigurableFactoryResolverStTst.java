package net.anotheria.anoprise.metafactory;

import org.junit.BeforeClass;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

public class ConfigurableFactoryResolverStTst {

	private static ConfigurableFactoryResolver resolver;

	@BeforeClass public static void setUp(){
		resolver = ConfigurableFactoryResolver.create();
	}

	@Test public void resolveAliasTest(){
		assertEquals(net.anotheria.anoprise.mock.TestServiceFactory.class, resolver.resolveFactory("net.anotheria.anoprise.mock.TestService"));		
		assertNull(resolver.resolveFactory("net.anotheria.anosite.gen.user.service.IUserService"));
	}
}