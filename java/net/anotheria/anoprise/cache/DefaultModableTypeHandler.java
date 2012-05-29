package net.anotheria.anoprise.cache;

/**
 * Default implementation for the ModableTypeHandler
 *
 * @author ivanbatura
 */
public class DefaultModableTypeHandler implements ModableTypeHandler {
	@Override
	public long getModableValue(Object parameter) {
		if (parameter == null)
			throw new AssertionError("Null objects are not supported");

		//Modable byte parameter
		if (parameter instanceof Byte)
			return Byte.class.cast(parameter).longValue();

		//Modable short parameter
		if (parameter instanceof Short)
			return Short.class.cast(parameter).longValue();

		//Modable int parameter
		if (parameter instanceof Integer)
			return Integer.class.cast(parameter).longValue();

		//Modable Long parameter
		if (parameter instanceof Long)
			return Long.class.cast(parameter);

		//Modable float parameter
		if (parameter instanceof Float)
			return Float.class.cast(parameter).longValue();

		//Modable double parameter
		if (parameter instanceof Double)
			return Double.class.cast(parameter).longValue();

		//Modable String parameter
		if (parameter instanceof String)
			return (long) String.class.cast(parameter).hashCode();

		throw new AssertionError("Object " + parameter + " not supported in current implementation. Please implement getModableValue for stored object");
	}
}
