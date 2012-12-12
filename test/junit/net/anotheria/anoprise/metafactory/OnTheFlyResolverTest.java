package net.anotheria.anoprise.metafactory;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 12.12.12 15:04
 */
public class OnTheFlyResolverTest {
	@Test
	public void testOnTheFlyResolver() throws MetaFactoryException{
		ServiceWithoutAFactory service = MetaFactory.get(ServiceWithoutAFactory.class);
		assertNotNull(service);
		//if we can call this method without exception, we are successful
		service.foo();

	}

	@Test
	public void testOnTheFlyResolverFailWithMultileImpls(){
		try{
			AnotherServiceWithoutAFactory service = MetaFactory.get(AnotherServiceWithoutAFactory.class);
			fail("We should have an exception here");
		}catch(MetaFactoryException e){
			//everything ok
		}
	}
}
