package net.anotheria.anoprise.mocking;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * Factory for mock objects.
 * @author lrosenberg.
 *
 */
public final class MockFactory {
	/**
	 * Creates a new Mock of type T.
	 * @param <T> interface type to mock.
	 * @param clazz class of the interface (T.class).
	 * @param mockings some mockings with method implementations for methods in T.
	 */
	public static <T> T createMock(Class<T> clazz, Mocking ... mockings){
		InvocationHandler mock = new Mock(clazz, Arrays.asList(mockings));
		return clazz.cast(Proxy.newProxyInstance(MockFactory.class.getClassLoader(), new Class[]{ clazz }, mock)); 		
	}
	
	private MockFactory(){
		//prevent initialization.
	}
}
