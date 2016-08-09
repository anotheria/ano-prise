package net.anotheria.anoprise.cache;

/**
 * ModableTypeHandler for calculation modable value for the stored key in fail over support cache
 *
 * @author ivanbatura
 */
public interface ModableTypeHandler {
	/**
	 * Calculate the modable value. If you plan to use not primitive type of the key please create you own implementation
	 * By the default supported types are: Byte, Short, Integer, Long, Float, Double, String
	 *
	 * @param parameter key parameter of the cache
	 * @return modable value
	 */
    long getModableValue(Object parameter);
}