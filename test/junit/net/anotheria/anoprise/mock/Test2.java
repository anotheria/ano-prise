package net.anotheria.anoprise.mock;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import net.anotheria.anoprise.mocking.MockFactory;
import net.anotheria.anoprise.mocking.Mocking;

import org.junit.Before;
import org.junit.Test;

public class Test2 {

	private static TestInterface test;
	
	@Before public void initTest(){
		test = MockFactory.createMock(TestInterface.class, 
			new Mocking(){
				public String askService(String param){
					return "Service said: "+param+" accepted.";
				}
			}
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
	
	public static class TestInterfaceMock implements Mocking{
		public String askService(String param){
			return "Service said: "+param+" accepted.";
		}
		
		public void foo(String notUsed){
			
		}
	}
}
