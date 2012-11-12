package net.anotheria.anoprise.metafactory.docs;

import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;

import org.configureme.ConfigurationManager;
import org.configureme.environments.DynamicEnvironment;
import org.junit.Before;
import org.junit.BeforeClass;

import static junit.framework.Assert.*;

public class VerySimpleTest {

	@BeforeClass
	public static void setConfigureMe(){
		ConfigurationManager.INSTANCE.setDefaultEnvironment(new DynamicEnvironment("test", "junit"));
	}

	@Before public void configureMetaFactory(){
		MetaFactory.reset();
		
		//first we have to configure all available services, this doesn't have to happen here, it's just easier to understand the test if it happens here.
		//we usually perform this initialization in a util class called by context-listener or from main method.
		MetaFactory.addFactoryClass("CalculatorService-Impl", CalculatorServiceFactory.class);
		MetaFactory.addFactoryClass("CalculatorService-Mock", CalculatorServiceMockFactory.class);
		//now both services are known, but which are we gonna use?
		
	}
	
	private void test() throws MetaFactoryException{
		CalculatorService service = MetaFactory.get(CalculatorService.class);
		assertEquals("Expected 4", 4, service.plus(2, 2));
	}
	
	
	@org.junit.Test public void testWithMock() throws MetaFactoryException{
		MetaFactory.addAlias("CalculatorService-Mock", CalculatorService.class.getName());
		test();
	}

	@org.junit.Test public void testWithImpl() throws MetaFactoryException{
		MetaFactory.addAlias("CalculatorService-Impl", CalculatorService.class.getName());
		test();
	}
}
 