package net.anotheria.anoprise.mock;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import net.anotheria.anoprise.mocking.MockFactory;
import net.anotheria.anoprise.mocking.Mocking;

import org.junit.Before;
import org.junit.Test;

/**
 * This test doesn't work, because Mock can't access a public method defined in an anonymous inner class.   
 * Therefor it's not included in the MockTestSuite. 
 * @author lrosenberg.
 *
 */
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
}
