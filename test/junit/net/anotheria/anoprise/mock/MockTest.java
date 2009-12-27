package net.anotheria.anoprise.mock;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import net.anotheria.anoprise.mocking.MockFactory;
import net.anotheria.anoprise.mocking.Mocking;

import org.junit.Before;
import org.junit.Test;

public class MockTest {

	private static TestInterface test;
	
	@Before public void initTest(){
		test = MockFactory.createMock(TestInterface.class, 
				new Mock1(), new Mock2(), new Mock3()
		);

	}
	
	@Test public void testMock(){
		String reply = test.askService("TESTPARAM");
		assertNotNull(reply);
		assertTrue(reply.indexOf("TESTPARAM")!=-1);
	}
	
	@Test public void testNotMockedMethod(){
		try{
			test.notImplementedMethod();
			fail("Test should fail.");
		}catch(IllegalArgumentException notMocked){}
	}
	
	@Test public void testCache(){
		//this test calls the same method more than once, ensuring the cache is covered.
		test.voidDummyMethod();
		test.voidDummyMethod();
		test.voidDummyMethod();
		test.voidDummyMethod();
		test.voidDummyMethod();
	}
	
	@Test public void testUnmocked(){
		assertNotNull(test.toString());
		assertFalse(test.equals(new String(".")));
	}
	
	
	@Test public void testException(){
		try{
			test.throwExceptionOnCall();
			fail("expected exceptions");
		}catch(TestException e){
			
		}
	}
	
	
	public static class Mock1 implements Mocking{
		public String askService(String param){
			return "Service said: "+param+" accepted.";
		}
		
		public void foo(String notUsed){
			
		}
	}
	
	public static class Mock2 implements Mocking{
		public void voidDummyMethod(){
			int a = 100;
			for (int i=0; i<1000; i++)
				a++;
		}
	}
	
	public static class Mock3 implements Mocking{
		public void throwExceptionOnCall() throws TestException{
			throw new TestException();
		}
	}
}
