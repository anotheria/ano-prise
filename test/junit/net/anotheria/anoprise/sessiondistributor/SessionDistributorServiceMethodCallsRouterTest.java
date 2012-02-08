package net.anotheria.anoprise.sessiondistributor;

import org.distributeme.core.ClientSideCallContext;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Simplest Junit test fro SD router!!!
 *
 * @author h3ll
 */
public class SessionDistributorServiceMethodCallsRouterTest {

	@Test
	public void testRoutesFlow() {
		SessionDistributorServiceMethodCallsRouter router = new SessionDistributorServiceMethodCallsRouter();
		ClientSideCallContext context = new ClientSideCallContext("createDistributedSession");
		context.setServiceId("simpleService");
		context.setParameters(Arrays.asList("qweqweqweqwefsdf"));
		SessionDistributorServiceConfig config = SessionDistributorServiceConfig.getInstance();
		String service = router.getServiceIdForCall(context);
		Assert.assertEquals("Only 1 node! do nothing!", service, "simpleService");

		//what with failing?
		context.setCallCount(2);
		service = router.getServiceIdForCall(context);
		Assert.assertEquals("Only 1 node! do nothing even on FAIL!", service, "simpleService");


		//adding another node!!
		context.setCallCount(0);
		config.setSessionDistributorServersAmount(2);
		service = router.getServiceIdForCall(context);
		Assert.assertTrue(service.contains("_"));
		Assert.assertTrue("Wrong behaviour!", "qweqweqweqwefsdf".hashCode() % 2 == 0 ? service.equals("simpleService_0") : service.equals("simpleService_1"));

		//what with not routable  by mod methods ? methods ???
		context.setMethodName("getDistributedSessionNames");
		String firstCall = router.getServiceIdForCall(context);
		String secondCall = router.getServiceIdForCall(context);
		//should be call to different services!!!
		Assert.assertFalse(firstCall.equals(secondCall));


		//failing case!!!
		context.setCallCount(2);
		context.setServiceId("simpleService_1");
		String call = router.getServiceIdForCall(context);
		Assert.assertTrue(call.contains("_") && call.lastIndexOf("_") == call.length() - 2);

		//when only 1 service!
		config.setSessionDistributorServersAmount(1);
		call = router.getServiceIdForCall(context);
		Assert.assertEquals(call, "simpleService");

	}


}
