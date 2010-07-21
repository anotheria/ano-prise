package net.anotheria.anoprise.metafactory.docs;

import net.anotheria.anoprise.metafactory.Extension;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;

import org.junit.Before;
import static junit.framework.Assert.*;

public class VerySimpleTestWithExtensions {
	@Before public void configureMetaFactory(){
		MetaFactory.reset();
		
		//first we have to configure all available services, this doesn't have to happen here, it's just easier to understand the test if it happens here.
		//we usually perform this initialization in a util class called by context-listener or from main method.
		//Instead of free names we are using 'EXTENSIONS' here. This allows us to perform lookup by class instead of a string.
		MetaFactory.addFactoryClass(CalculatorService.class, Extension.DOMAIN,   CalculatorServiceFactory.class);
		MetaFactory.addFactoryClass(CalculatorService.class, Extension.FIXTURE, CalculatorServiceMockFactory.class);
		//now both services are known, but which are we gonna use?
		
	}
	
	private void test() throws MetaFactoryException{
		//no need to cast and compile time safety, that you'll get something of your type.
		CalculatorService service = MetaFactory.get(CalculatorService.class);
		assertEquals("Expected 4", 4, service.plus(2, 2));
	}
	
	@org.junit.Test public void testWithImpl() throws MetaFactoryException{
		//this line basically says, the LOCAL variant of CalculatorService is now the default one.
		MetaFactory.addAlias(CalculatorService.class, Extension.DOMAIN);
		test();
	}

	@org.junit.Test public void testWithMock() throws MetaFactoryException{
		//this line basically says, the FIXTURE variant of CalculatorService is now the default one.
		MetaFactory.addAlias(CalculatorService.class, Extension.FIXTURE);
		test();
	}
}
