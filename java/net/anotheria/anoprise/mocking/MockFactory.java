package net.anotheria.anoprise.mocking;

import java.lang.reflect.Proxy;
import java.util.Arrays;

public class MockFactory {
	public static <T> T createMock(Class<T> clazz, Mocking ... mockeries){
		Mock mock = new Mock(clazz, Arrays.asList(mockeries));
		return clazz.cast(Proxy.newProxyInstance(MockFactory.class.getClassLoader(), new Class[]{ clazz }, mock)); 		
	}
}
