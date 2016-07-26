package net.anotheria.anoprise.mocking;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This object presents the on-the-fly created implementation of an interface by the mock factory.
 * @author lrosenberg.
 *
 */
public class Mock implements InvocationHandler{
	
	/**
	 * List of mocked classes. Contains at least one class, the mocked target interface. Contains also all superinterfaces to ensure proper method mapping.
	 */
	private List<Class<?>> targets;
	/**
	 * List of mocking objects which are implementing different methods of the target class.
	 */
	private List<Mocking> mockings;
	/**
	 * Internal cache to reduce lookups.
	 */
	private Map<Method, MockingAndMethod> cache = new HashMap<>();

	/**
	 * Creates a new mock.
	 * @param aTarget target class.
	 * @param someMockings list of mockings.
	 */
	Mock(Class<?> aTarget, List<Mocking> someMockings){
		mockings = someMockings;
		targets = findInterfaces(aTarget);
	}
	
	private static List<Class<?>> findInterfaces(Class<?> source){
		List<Class<?>> ret = new ArrayList<>();
		ret.add(source);
		Class<?>[] interfaces = source.getInterfaces();
		for (Class<?> i : interfaces)
			ret.addAll(findInterfaces(i));
		return ret;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		
		if (!targets.contains(method.getDeclaringClass())){
			return method.invoke(this, args);
		}
		
		MockingAndMethod implementor = findImplementor(method);
		if (implementor==null)
			throw new IllegalArgumentException("Method: "+method+" is not mocked");

		try{
            return implementor.getMethod().invoke(implementor.getMocking(), args);
		}catch(InvocationTargetException e){
			throw e.getCause();
		}
	}
	
	/**
	 * Finds an implementation for the given method in the supplied mockings. 
	 * @param method method to search.
	 * @return Mocking and Method pair.
	 */
	private MockingAndMethod findImplementor(Method method){
		
		MockingAndMethod fromCache = cache.get(method);
		if (fromCache!=null)
			return fromCache;
		
		for (Mocking m : mockings){
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
	
	/**
	 * Returns true if the both methods have same name, same return type and same parameters.
	 * @param first first method to compare.
	 * @param second second method to compare.
	 * @return true if the methods are equal (even they are declared in different classes).
	 */
	private static boolean areMethodsEqual(Method first, Method second){
		if (!(first.getName().equals(second.getName()))){
			return false;
		}
		
		if (!(first.getReturnType().equals(second.getReturnType()))){
			return false;
		}
		
		Class<?>[] firstParameters  = first.getParameterTypes();
		Class<?>[] secondParameters = second.getParameterTypes();
		return Arrays.equals(firstParameters, secondParameters);
	}
	
	/**
	 * Holder class for a Mocking which implements a method, and the method it implements.
	 * @author lrosenberg.
	 *
	 */
	private static class MockingAndMethod{
		/**
		 * The mocking object.
		 */
		private Mocking mocking;
		/**
		 * Method implementation in the mocking.
		 */
		private Method method;
		
		/**
		 * Creates a new mockingandmethod pair.
		 * @param aMocking
		 * @param aMethod
		 */
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
