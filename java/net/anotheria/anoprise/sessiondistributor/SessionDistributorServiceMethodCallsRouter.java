package net.anotheria.anoprise.sessiondistributor;

import org.apache.log4j.Logger;
import org.distributeme.core.ClientSideCallContext;
import org.distributeme.core.failing.FailDecision;
import org.distributeme.core.failing.FailingStrategy;
import org.distributeme.core.routing.Router;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Complex call-router for SessionDistributorService.
 *
 * @author h3ll
 */
public class SessionDistributorServiceMethodCallsRouter implements Router, FailingStrategy {

	/**
	 * {@link Logger} instance.
	 */
	private static final Logger LOG = Logger.getLogger(SessionDistributorServiceMethodCallsRouter.class);

	/**
	 * Routing parameter position in method definition.
	 * For current router - SessionId (hashCode) will be used as main parameter for mod-routing,
	 * please {@see SessionDistributorService} interface.
	 * <p>If interface will be changed - please review current router.</p>
	 */
	private static final int PARAMETER_POSITION = 0;
	/**
	 * MethodNames to route.
	 * Should contains only SDS interface methods names - which has some incoming parameters - and should be
	 * routed by MOD.
	 */
	private final static Set<String> routeModMethods = new HashSet<String>();
	/**
	 * Under line constant.
	 */
	private static final String UNDER_LINE = "_";

	/**
	 * Delegation counter.
	 */
	private AtomicInteger delegateCallCounter;

	/**
	 * {@link SessionDistributorServiceConfig} instance.
	 */
	private SessionDistributorServiceConfig configuration;

	/**
	 * Static init block.
	 * Initializing methods which should be routed by mod.
	 */
	static {
		routeModMethods.add("createDistributedSession");
		routeModMethods.add("deleteDistributedSession");
		routeModMethods.add("restoreDistributedSession");
		routeModMethods.add("updateSessionUserId");
		routeModMethods.add("updateSessionEditorId");
		routeModMethods.add("addDistributedAttribute");
		routeModMethods.add("removeDistributedAttribute");
		routeModMethods.add("keepDistributedSessionAlive");
	}


	/**
	 * Constructor.
	 */
	public SessionDistributorServiceMethodCallsRouter() {
		this.configuration = SessionDistributorServiceConfig.getInstance();
		this.delegateCallCounter = new AtomicInteger(0);
	}

	@Override
	public FailDecision callFailed(ClientSideCallContext clientSideCallContext) {
		//check that failing strategy is enabled!
		if (!configuration.isFailingStrategyEnabled())
			return FailDecision.fail();


		if (clientSideCallContext.getCallCount() <= configuration.getSessionDistributorServersAmount())
			return FailDecision.retry();

		return FailDecision.fail();
	}


	@Override
	public String getServiceIdForCall(ClientSideCallContext callContext) {
		//Checking if MOD routing is possible for current call!
		if (routeModMethods.contains(callContext.getMethodName()) && callContext.isFirstCall())
			return getModBasedServiceId(callContext);

		return getRoundRobinBasedServiceId(callContext);
	}

	/**
	 * Returns  service id - based on mod routing.
	 *
	 * @param callContext {@link ClientSideCallContext}
	 * @return service id which should be called
	 */
	@SuppressWarnings({"PointlessArithmeticExpression"})
	private String getModBasedServiceId(ClientSideCallContext callContext) {
		List<?> parameters = callContext.getParameters();
		if (parameters.size() < PARAMETER_POSITION + 1)
			throw new AssertionError("Not properly configured router, parameter count is less than expected - actual: " + parameters.size() + ", expected: " + PARAMETER_POSITION);

		Object parameter = parameters.get(PARAMETER_POSITION);
		long parameterValue = getModableValue(parameter);
		//reading mod from config
		int mod = configuration.getSessionDistributorServersAmount();

		String result = (mod - 1) <= 0 ? callContext.getServiceId() : callContext.getServiceId() + UNDER_LINE + (parameterValue % mod);
		if (LOG.isDebugEnabled())
			LOG.debug("Returning mod based result : " + result + " for " + callContext);
		return result;
	}

	/**
	 * Returns  service id - based on mod RoundRobin routing. Actually for failing strategy and not mod-routed methods..
	 *
	 * @param callContext {@link ClientSideCallContext}
	 * @return service id which should be called
	 */
	private String getRoundRobinBasedServiceId(ClientSideCallContext callContext) {
		//RoundRobin - for delegate Call!
		int callCounter = delegateCallCounter.get();
		int mod = configuration.getSessionDistributorServersAmount();
		if (delegateCallCounter.incrementAndGet() > (mod - 1))
			delegateCallCounter.set(0);
		String serviceId = callContext.getServiceId();
		//for failing strategy!
		if (!callContext.isFirstCall()) {
			//find last index of "_" cause after failing serviceId will contains smth like "_{mod}"
			int instanceNumberPosition = serviceId.lastIndexOf(UNDER_LINE);
			serviceId = instanceNumberPosition > 0 ? serviceId.substring(0, instanceNumberPosition) : serviceId;
		}
		String result = (mod - 1) <= 0 ? serviceId : serviceId + UNDER_LINE + callCounter;

		if (LOG.isDebugEnabled())
			LOG.debug("Returning roundRobin based result : " + result + " for " + callContext);

		return result;
	}


	/**
	 * Returns parameter value converted to Long actually.
	 * For further <a>mod</a> operation.
	 *
	 * @param o object to convert
	 * @return long value
	 */
	private long getModableValue(Object o) {
		if (o instanceof String)
			return Math.abs(String.class.cast(o).hashCode());
		if (o == null)
			throw new AssertionError("Null objects are not supported");

		throw new AssertionError("Object " + o + " of type " + o.getClass() + " is not supported. Please use String");
	}

	@Override
	public void customize(String parameter) {
		//in current implementation it's not required! at all :)
	}
}
