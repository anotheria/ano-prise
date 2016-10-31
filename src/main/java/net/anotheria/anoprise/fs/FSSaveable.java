package net.anotheria.anoprise.fs;

import java.io.Serializable;

/**
 * Interface of the file system service.
 * 
 * @author abolbat
 * @version 1.0, 2010/02/11
 */
public interface FSSaveable extends Serializable {

	/**
	 * Returns the owner id of the {@link FSSaveable} object.
	 * 
	 * @return {@link String}
	 */
	String getOwnerId();

	/**
	 * Returns the owner file name of the {@link FSSaveable} object.
	 *
	 * @return {@link String}
	 */
	String getFileOwnerId();

	/**
	 * Returns the owner directory path of the {@link FSSaveable} object.
	 *
	 * @return {@link String}
	 */
	String getDirOwnerId();
}
