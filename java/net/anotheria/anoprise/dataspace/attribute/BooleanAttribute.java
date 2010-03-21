package net.anotheria.anoprise.dataspace.attribute;

/**
 * Boolean attribute used in dataspace.
 * 
 * @author abolbat
 */
public class BooleanAttribute extends Attribute {

	/**
	 * Basic serialVersionUID variable.
	 */
	private static final long serialVersionUID = 7580990463274456724L;

	/**
	 * Attribute long value.
	 */
	private boolean value;

	/**
	 * Default constructor.
	 * 
	 * @param aName
	 *            - attribute name
	 * @param aStringValue
	 *            - attribute value as string
	 */
	public BooleanAttribute(String aName, String aStringValue) {
		super(aName);
		this.value = Boolean.parseBoolean(aStringValue);
	}

	/**
	 * Default constructor.
	 * 
	 * @param aName
	 *            - attribute name
	 * @param aValue
	 *            - attribute value
	 */
	public BooleanAttribute(String aName, boolean aValue) {
		super(aName);
		this.value = aValue;
	}

	@Override
	public String getValueAsString() {
		return "" + value;
	}

	@Override
	public AttributeType getType() {
		return AttributeType.BOOLEAN;
	}

	public void setValue(boolean aValue) {
		this.value = aValue;
	}

	public boolean getValue() {
		return value;
	}

}
