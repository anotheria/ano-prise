package net.anotheria.anoprise.metafactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract {@link net.anotheria.anoprise.metafactory.ParameterizedServiceFactory} implementation.
 *
 * @author Alexandr Bolbat
 * @version $Id: $Id
 */
public abstract class AbstractParameterizedServiceFactory<T extends Service> implements ParameterizedServiceFactory<T> {

	/**
	 * Factory parameters.
	 */
	private Map<String, Serializable> parameters;

	/** {@inheritDoc} */
	@Override
	public void setParameters(final Map<String, Serializable> aParameters) {
		this.parameters = aParameters;
	}

	/**
	 * Add parameter.
	 *
	 * @param parameterName
	 *            parameter name
	 * @param parameterValue
	 *            parameter value
	 */
	public void addParameter(final String parameterName, final Serializable parameterValue) {
		if (parameterName == null || parameterName.trim().isEmpty() || parameterValue == null)
			return;

		if (parameters == null)
			synchronized (this) {
				if (parameters == null)
					parameters = new HashMap<String, Serializable>();
			}

		parameters.put(parameterName, parameterValue);
	}

	/**
	 * Get all parameters map.
	 *
	 * @return {@link java.util.Map}
	 */
	protected Map<String, Serializable> getParameters() {
		return parameters != null ? new HashMap<String, Serializable>(parameters) : new HashMap<String, Serializable>();
	}

	/**
	 * Get all parameters names.
	 *
	 * @return {@link java.util.List} of {@link java.lang.String}
	 */
	protected List<String> getParametersNames() {
		return parameters != null ? new ArrayList<String>(parameters.keySet()) : new ArrayList<String>();
	}

	/**
	 * Get parameter value.
	 *
	 * @param parameterName
	 *            parameter name
	 * @return {@link java.io.Serializable} value or empty {@link java.lang.String} if parameter not exist
	 */
	protected Serializable getParameterValue(final String parameterName) {
		if (parameterName == null || parameterName.trim().isEmpty() || parameters == null)
			return "";

		Serializable value = parameters.get(parameterName);
		return value != null ? value : "";
	}

	/**
	 * Get parameter value as {@link java.lang.String}.
	 *
	 * @param parameterName
	 *            parameter name
	 * @return {@link java.io.Serializable} value or empty {@link java.lang.String} if parameter not exist
	 */
	protected String getParameterValueAsString(final String parameterName) {
		return String.valueOf(getParameterValue(parameterName));
	}

}
