package net.anotheria.anoprise.mock;

import org.junit.Test;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;

import net.anotheria.anoprise.mocking.MockFactory;
import net.anotheria.anoprise.mocking.Mocking;

public class MockErrorsTest {
	
	@Test public void testWrongParameterType(){
		TestInterface test = MockFactory.createMock(TestInterface.class, new WrongParameterTypeMock());
		test(test);
	}
	
	@Test public void testWrongParameterCount(){
		TestInterface test = MockFactory.createMock(TestInterface.class, new WrongParameterCountMock());
		test(test);
	}

	@Test public void testWrongReturnType(){
		TestInterface test = MockFactory.createMock(TestInterface.class, new WrongReturnTypeMock());
		test(test);
	}

	@Test public void testWrongName(){
		TestInterface test = MockFactory.createMock(TestInterface.class, new WrongNameMock());
		test(test);
	}
	
	/**
	 * This test ensures that the previous tests failed for proper reasons - not finding the method. If the method matching in Mock.java is broken, the ProperMock implementation will be hidden by the 
	 * wrong-mockings and this test will fail.
	 */
	@Test public void testForProperMethodMatching(){
		TestInterface test = MockFactory.createMock(TestInterface.class, new WrongNameMock(), new WrongParameterCountMock(), new WrongParameterTypeMock(), new WrongReturnTypeMock(), new ProperMock());
		assertTrue(test.askService("").equals("OK"));
	}
	
	private void test(TestInterface testInstance){
		try{
			testInstance.askService("foo");
			fail("expected illegal argument exception.");
		}catch(IllegalArgumentException e){
		}
	}

	public static class WrongParameterTypeMock implements Mocking{
		public String askService(int intParamInsteadOfString){
			return null;
		}
	}
	public static class WrongParameterCountMock implements Mocking{
		public String askService(boolean firstParam, boolean secondParam){
			return null;
		}
	}
	public static class WrongReturnTypeMock implements Mocking{
		public int askService(String aParam){
			return 0;
		}
	}
	public static class WrongNameMock implements Mocking{
		public String aSkService(String aParam){
			return "";
		}
	}
	public static class ProperMock implements Mocking{
		public String askService(String aParam){
			return "OK";
		}
	}
}
