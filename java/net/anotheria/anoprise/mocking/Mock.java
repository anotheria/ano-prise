package net.anotheria.anoprise.mocking;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mock implements InvocationHandler{
	
	private Class<?> target;
	private List<Mocking> mockeries;
	
	private Map<Method, MockingAndMethod> cache = new HashMap<Method, MockingAndMethod>();

	Mock(Class<?> aTarget, List<Mocking> someMockeries){
		target = aTarget;
		mockeries = someMockeries;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		
		if (!method.getDeclaringClass().equals(target)){
			return method.invoke(this, args);
		}
		
		MockingAndMethod implementor = findImplementor(method);
		if (implementor==null)
			throw new IllegalArgumentException("Method: "+method+" is not mocked");
		
		try{
			Object ret = implementor.getMethod().invoke(implementor.getMocking(), args);
			return ret;
		}catch(Exception e){
			e.printStackTrace();
			throw e.getCause();
		}
	}
	
	private MockingAndMethod findImplementor(Method method){
		
		MockingAndMethod fromCache = cache.get(method);
		if (fromCache!=null)
			return fromCache;
		
		for (Mocking m : mockeries){
			Class<?> mClazz = m.getClass();
			Method[] methods = mClazz.getDeclaredMethods();
			for (Method aMethod : methods){
				if (areMethodsEqual(aMethod, method)){
					MockingAndMethod mam = new MockingAndMethod(m, aMethod);
					cache.put(method, mam);
					return mam;
				}
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
	
	private static class MockingAndMethod{
		Mocking mocking;
		Method method;
		
		MockingAndMethod(Mocking aMocking, Method aMethod){
			mocking = aMocking;
			method  = aMethod;
		}
		
		Mocking getMocking(){
			return mocking;
		}
		
		Method getMethod(){
			return method;
		}
	}
	
}
