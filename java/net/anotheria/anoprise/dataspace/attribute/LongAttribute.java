package net.anotheria.anoprise.dataspace.attribute;

/**
 * Long attribute used in dataspace.
 * 
 * @author abolbat
 */
public class LongAttribute extends Attribute {

	/**
	 * Basic serialVersionUID variable.
	 */
	private static final long serialVersionUID = 4308729762623979292L;

	/**
	 * Attribute long value.
	 */
	private long value;

	/**
	 * Default constructor.
	 * 
	 * @param aName
	 *            - attribute name
	 * @param aStringValue
	 *            - attribute value as string
	 */
	public LongAttribute(String aName, String aStringValue) {
		super(aName);
		this.value = Long.parseLong(aStringValue);
	}

	/**
	 * Default constructor.
	 * 
	 * @param aName
	 *            - attribute name
	 * @param aValue
	 *            - attribute value
	 */
	public LongAttribute(String aName, long aValue) {
		super(aName);
		this.value = aValue;
	}

	@Override
	public String getValueAsString() {
		return String.valueOf(value);
	}

	@Override
	public AttributeType getType() {
		return AttributeType.LONG;
	}

	public void setValue(long aValue) {
		this.value = aValue;
	}

	public long getValue() {
		return value;
	}

}
