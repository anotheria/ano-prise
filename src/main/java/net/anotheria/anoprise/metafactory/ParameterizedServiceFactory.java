package net.anotheria.anoprise.metafactory;

import java.io.Serializable;
import java.util.Map;

/**
 * Parameterized service factory.
 * 
 * @author Alexandr Bolbat
 * 
 * @param <T>
 */
public interface ParameterizedServiceFactory<T extends Service> extends ServiceFactory<T> {

	/**
	 * Set {@link ParameterizedServiceFactory} parameters.
	 * 
	 * @param parameters
	 *            parameters map
	 */
	void setParameters(Map<String, Serializable> parameters);
}
