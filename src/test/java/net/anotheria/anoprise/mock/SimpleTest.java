package net.anotheria.anoprise.mock;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import net.anotheria.anoprise.mocking.MockFactory;
import net.anotheria.anoprise.mocking.Mocking;

import org.junit.Before;
import org.junit.Test;

/**
 * This is the simplies possible test for mocking.
 * @author lrosenberg.
 *
 */
public class SimpleTest {
	/**
	 * Implementation of the mocking. Since we only need the askService method, we will only implement it, and no further method.
	 * @author another
	 *
	 */
	public static class MyMocking implements Mocking{
		public String askService(String param){
			return "Service said: "+param+" accepted.";
		}
	}
	
	private TestInterface test;
	
	@Before public void initTest(){
		test = MockFactory.createMock(TestInterface.class, new MyMocking());

	}
	
	/**
	 * This is a working test.
	 */
	@Test public void testMock(){
		/**
		 * Since askService is mocked, we should get a reply, like: "Service said: TESTPARAM accepted."
		 */
		String reply = test.askService("TESTPARAM");
		assertNotNull(reply);
		assertTrue(reply.indexOf("TESTPARAM")!=-1);
	}
	/**
	 * This test has to fail, cause the notImplementedMethod is actually not implemented ;-).
	 */
	@Test public void testNotMockedMethod(){
		try{
			test.notImplementedMethod();
			fail("Test should fail.");
		}catch(IllegalArgumentException notMocked){}
	}
}
