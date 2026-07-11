package net.anotheria.anoprise.metafactory;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 12.12.12 15:04
 */
public class OnTheFlyResolverTest {

	@BeforeEach
	@AfterEach
	public void reset(){
		MetaFactory.reset();
	}

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

	@Test
	public void testOnTheFlyResolverWithMultipleImplsConflictResolver() throws Exception{
		final String targetClazzName = AnotherServiceWithoutAFactoryImpl1.class.getSimpleName();
		MetaFactory.addOnTheFlyConflictResolver(new OnTheFlyConflictResolver() {
			@Override
			public <T> Class<? extends T> resolveConflict(Collection<Class<? extends T>> candidates) {
				for (Class<? extends T> clazz : candidates){
					if (clazz.getSimpleName().equals(targetClazzName))
						return clazz;
				}
				return null;
			}
		});
		AnotherServiceWithoutAFactory service = MetaFactory.get(AnotherServiceWithoutAFactory.class);
		assertNotNull(service);
		//if we can call this method without exception, we are successful
		service.foo();

	}

	@Test
	public void testOnTheFlyResolverFailWithMultileImplsAfterReset(){
		//tests whether previous test breaks resolving mechanism.
		testOnTheFlyResolverFailWithMultileImpls();
	}

}
