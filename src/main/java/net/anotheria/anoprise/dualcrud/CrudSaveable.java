package net.anotheria.anoprise.dualcrud;

import java.io.Serializable;

/**
 * Interface for object saveable by a CrudService. 
 * @author lrosenberg
 */
public interface CrudSaveable extends Serializable{
	/**
	 * Returns the id of the owner of the CrudSaveable object.
	 * @return a string representation of an id object.
	 */
	String getOwnerId();
}
