package net.anotheria.anoprise.cache;

import net.anotheria.util.StringUtils;

/**
 * Helper for Mod routing.
 * Actually using this we can simply check call  - failOver/normal, in case of failOver to next node.
 *
 * @author ivanbatura
 */
public final class FailOverHelper {
	private static final int DEFAULT_INSTANCE_NUMBER_FOR_NON_CLUSTERED_SERVICES = -1;

	/**
	 * Constructor.
	 */
	private FailOverHelper() {
		throw new IllegalAccessError("Can't be instantiated");
	}

	/**
	 * AS Mod routing is parameter based we can simply check either call is normal MOD call - or failOver.
	 * True will be returned only in case if some call is FAIL-Over to next node call.
	 * False will be returned if there is no multiple instances of some service (or systemPropertyName is wrong),
	 * or in case when call is not a failOver to next node call.
	 *
	 * @param systemPropertyName actually SystemProperty name under which node id is passed to some DieME service ( "extension" almost for all parship stuff)
	 * @param serviceAmount	  amount of services ( nodes in the cluster)
	 * @param modableValue	   value for mod counting ( same date which is counted inside *ModRouter for service)
	 * @return boolean value
	 */
	public static boolean isFailOverCall(String systemPropertyName, int serviceAmount, long modableValue) {
		int currentInstanceNumber = getCurrentServiceInstanceNumber(systemPropertyName);
		return isMultipleInstancesEnabled(currentInstanceNumber) && currentInstanceNumber != (modableValue % serviceAmount);
}

	/**
	 * Return true if clustering / multiple nodes started.
	 *
	 * @param number currently calculated by "getCurrentServiceInstanceNumber" method call - value
	 * @return boolean
	 */
	private static boolean isMultipleInstancesEnabled(int number) {
		return number != DEFAULT_INSTANCE_NUMBER_FOR_NON_CLUSTERED_SERVICES;
	}

	/**
	 * Return SystemProperty value with given "systemPropertyName". If no such property exists, or it's value is NULL -
	 * "DEFAULT_INSTANCE_NUMBER_FOR_NON_CLUSTERED_SERVICES" will be returned, and this means that cluster not started, or  "systemPropertyName" wrong.
	 *
	 * @param systemPropertyName actually SystemProperty name under which node id is passed to some DieME service ( "extension" almost for all parship stuff)
	 * @return current node number
	 */
	public static int getCurrentServiceInstanceNumber(String systemPropertyName) {
		String modString = System.getProperty(systemPropertyName);
		if (StringUtils.isEmpty(modString))
			return DEFAULT_INSTANCE_NUMBER_FOR_NON_CLUSTERED_SERVICES;
		try {
			return Integer.parseInt(modString);
		} catch (NumberFormatException e) {
			return DEFAULT_INSTANCE_NUMBER_FOR_NON_CLUSTERED_SERVICES;
		}
	}
}
