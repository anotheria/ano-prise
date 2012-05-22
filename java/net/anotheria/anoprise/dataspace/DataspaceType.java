package net.anotheria.anoprise.dataspace;

import java.io.Serializable;

/**
 * DataspaceType interface used in DataspaceService.
 * 
 * @author abolbat
 */
public interface DataspaceType extends Serializable{

	/**
	 * Get dataspace type id.
	 * 
	 * @return id
	 */
	int getId();

	/**
	 * Get dataspace type name.
	 * 
	 * @return id
	 */
	String getName();

}
