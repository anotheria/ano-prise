package net.anotheria.anoprise.dualcrud;

import java.io.Serializable;

/**
 * <p>Query interface.</p>
 *
 * @author another
 * @version $Id: $Id
 */
public interface Query extends Serializable {

	/**
	 * <p>getName.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	String getName();

	/**
	 * <p>getValue.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	String getValue();

}
