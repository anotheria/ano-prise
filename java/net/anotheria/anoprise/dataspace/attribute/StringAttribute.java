package net.anotheria.anoprise.dataspace.attribute;

/**
 * String attribute used in dataspace.
 * 
 * @author abolbat
 */
public class StringAttribute extends Attribute {

	/**
	 * Basic serialVersionUID variable.
	 */
	private static final long serialVersionUID = 5071830587468149209L;

	/**
	 * Attribute long value.
	 */
	private String value;

	/**
	 * Default constructor.
	 * 
	 * @param aName
	 *            - attribute name
	 * @param aStringValue
	 *            - attribute value as string
	 */
	public StringAttribute(String aName, String aStringValue) {
		super(aName);
		this.value = aStringValue;
	}

	@Override
	public String getValueAsString() {
		return "" + value;
	}

	@Override
	public AttributeType getType() {
		return AttributeType.STRING;
	}

	public void setValue(String aValue) {
		this.value = aValue;
	}

	public String getValue() {
		return value;
	}

}
