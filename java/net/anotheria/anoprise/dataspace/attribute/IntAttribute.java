package net.anotheria.anoprise.dataspace.attribute;

/**
 * Integer attribute used in dataspace.
 * 
 * @author abolbat
 */
public class IntAttribute extends Attribute {

	/**
	 * Basic serialVersionUID variable.
	 */
	private static final long serialVersionUID = -1951340263540832540L;

	/**
	 * Attribute long value.
	 */
	private int value;

	/**
	 * Default constructor.
	 * 
	 * @param aName
	 *            - attribute name
	 * @param aStringValue
	 *            - attribute value as string
	 */
	public IntAttribute(String aName, String aStringValue) {
		super(aName);
		this.value = Integer.parseInt(aStringValue);
	}

	/**
	 * Default constructor.
	 * 
	 * @param aName
	 *            - attribute name
	 * @param aValue
	 *            - attribute value
	 */
	public IntAttribute(String aName, int aValue) {
		super(aName);
		this.value = aValue;
	}

	@Override
	public String getValueAsString() {
		return "" + value;
	}

	@Override
	public AttributeType getType() {
		return AttributeType.INT;
	}

	public void setValue(int aValue) {
		this.value = aValue;
	}

	public int getValue() {
		return value;
	}

}
