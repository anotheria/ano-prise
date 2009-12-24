package net.anotheria.anoprise.mocking;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

public class Mock implements InvocationHandler{
	
	private Class<?> target;
	private List<Mockery> mockeries;

	Mock(Class<?> aTarget, List<Mockery> someMockeries){
		target = aTarget;
		mockeries = someMockeries;
		
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		
		if (!method.getDeclaringClass().equals(target)){
			return method.invoke(this, args);
		}
		
		MockeryAndMethod implementor = findImplementor(method);
		if (implementor==null)
			throw new IllegalArgumentException("Method: "+method+" is not mocked");
		return implementor.method.invoke(implementor.mockery, args);
	}
	
	private MockeryAndMethod findImplementor(Method method){
		for (Mockery m : mockeries){
			//System.out.println("checking "+m);
			Class<?> mClazz = m.getClass();
			Method[] methods = mClazz.getDeclaredMethods();
			for (Method mm : methods){
				if (areMethodsEqual(mm, method))
					return new MockeryAndMethod(m, mm);
			}
		}
		return null;
	}
	
	private boolean areMethodsEqual(Method first, Method second){
		if (!(first.getName().equals(second.getName())))
			return false;
		
		if (!(first.getReturnType().equals(second.getReturnType())))
			return false;
		
		return true;
	}
	
	private static class MockeryAndMethod{
		Mockery mockery;
		Method method;
		
		MockeryAndMethod(Mockery aMockery, Method aMethod){
			mockery = aMockery;
			method  = aMethod;
		}
	}
	
}
