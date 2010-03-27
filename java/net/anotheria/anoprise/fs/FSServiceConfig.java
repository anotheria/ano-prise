package net.anotheria.anoprise.fs;

import java.io.File;
import java.io.Serializable;

/**
 * Interface of the file system service.
 * 
 * @author abolbat
 * @version 1.0, 2010/02/11
 */
public final class FSServiceConfig implements Serializable {

	/**
	 * Basic serialVersionUID variable.
	 */
	private static final long serialVersionUID = -2629878661534470687L;

	/**
	 * Root folder in file system for storing service files.
	 */
	private String rootFolderPath;

	/**
	 * File extension for storing files.
	 */
	public static final String DEFAULT_FILE_EXTENSION = "dat";

	/**
	 * Configurable file Extension.
	 */
	private String fileExtension;

	/**
	 * Default maximum owner id length.
	 */
	public static final int DEFAULT_MAX_OWNER_ID_LENGTH = 10;

	/**
	 * Maximum owner id length.
	 */
	private int maxOwnerIdLength;

	/**
	 * Default fragment length.
	 */
	public static final int DEFAULT_FRAGMENT_LENGTH = 2;

	/**
	 * Fragment length.
	 */
	private int fragmetLegth;

	/**
	 * Text prefix for validation exception message.
	 */
	public static final String VALIDATION_ERROR_PREFIX = "Validation error: ";

	/**
	 * Default constructor.
	 * 
	 * @param aRootFolderPath
	 *            - root folder in file system for storing service files
	 * @throws FSServiceConfigException
	 */
	public FSServiceConfig(String aRootFolderPath) throws FSServiceConfigException {
		this(aRootFolderPath, DEFAULT_FILE_EXTENSION, DEFAULT_MAX_OWNER_ID_LENGTH, DEFAULT_FRAGMENT_LENGTH);
	}

	/**
	 * Default constructor.
	 * 
	 * @param aRootFolderPath
	 *            - root folder in file system for storing service files
	 * @param aFileExtension
	 *            - file extension
	 * @throws FSServiceConfigException
	 */
	public FSServiceConfig(String aRootFolderPath, String aFileExtension) throws FSServiceConfigException {
		this(aRootFolderPath, aFileExtension, DEFAULT_MAX_OWNER_ID_LENGTH, DEFAULT_FRAGMENT_LENGTH);
	}

	/**
	 * Default constructor.
	 * 
	 * @param aRootFolderPath
	 *            - root folder in file system for storing service files
	 * @param aFileExtension
	 *            - file extension
	 * @param aMaxOwnerIdLength
	 *            - maximum owner id length
	 * @param aFragmentLength
	 *            - a fragment length
	 * @throws FSServiceConfigException
	 */
	public FSServiceConfig(String aRootFolderPath, String aFileExtension, int aMaxOwnerIdLength, int aFragmentLength) throws FSServiceConfigException {
		this.rootFolderPath = validateRootFilderPath(aRootFolderPath);
		this.fileExtension = validateFileExtension(aFileExtension);
		this.maxOwnerIdLength = aMaxOwnerIdLength;
		this.fragmetLegth = aFragmentLength;
	}

	public String getRootFolderPath() {
		return rootFolderPath;
	}

	public String getFileExtension() {
		return fileExtension;
	}

	/**
	 * Return store file name.
	 * 
	 * @param ownerId
	 *            - owner id
	 * @param aFileExtension
	 *            - file extension
	 * @return file name
	 * @throws FSServiceConfigException
	 */
	public static final String getStoreFileName(String ownerId, String aFileExtension) throws FSServiceConfigException {
		return validateOwnerId(ownerId) + "." + aFileExtension;
	}

	/**
	 * Return store file name.
	 * 
	 * @param ownerId
	 *            - owner id
	 * @return file name
	 * @throws FSServiceConfigException
	 */
	public String getStoreFileName(String ownerId) throws FSServiceConfigException {
		return getStoreFileName(ownerId, fileExtension);
	}

	/**
	 * Internal method for fragmenting owner id by parameters.
	 * 
	 * @param ownerId
	 *            - owner id
	 * @param maxOwnerIdLength
	 *            - max owner id length
	 * @param fragmentLength
	 *            - fragment length
	 * @return fragments
	 */
	private static final String[] fragmentOwnerId(String ownerId, int maxOwnerIdLength, int fragmentLength) {
		if (ownerId == null || ownerId.length() == 0)
			throw new IllegalArgumentException("OwnerId is null or empty");

		while (ownerId.length() < maxOwnerIdLength)
			ownerId = "0" + ownerId;

		while (ownerId.length() % fragmentLength != 0)
			ownerId = "0" + ownerId;

		int fragmentationDepth = ownerId.length() / fragmentLength;
		String ret[] = new String[fragmentationDepth - 1];
		for (int i = 0; i < fragmentationDepth - 1; i++) {
			String fragment = ownerId.substring(i * fragmentLength, i * fragmentLength + fragmentLength);
			ret[i] = fragment;
		}

		return ret;
	}

	/**
	 * Return store folder path.
	 * 
	 * @param ownerId
	 *            - owner id
	 * @param maxOwnerIdLength
	 *            - max owner id length
	 * @param fragmentLength
	 *            - fragments length
	 * @return folder name
	 * @throws FSServiceConfigException
	 */
	public static final String getStoreFolderPath(String ownerId, int maxOwnerIdLength, int fragmentLength) throws FSServiceConfigException {
		String id = validateOwnerId(ownerId);
		String[] fragments = fragmentOwnerId(id, maxOwnerIdLength, fragmentLength);
		StringBuilder ret = new StringBuilder();
		for (String f : fragments) {
			ret.append(f).append(File.separatorChar);
		}
		return ret.toString();
	}

	/**
	 * Return store folder path.
	 * 
	 * @param ownerId
	 *            - owner id
	 * @return folder name
	 * @throws FSServiceConfigException
	 */
	public String getStoreFolderPath(String ownerId) throws FSServiceConfigException {
		String path = rootFolderPath;
		String lastChar = path.substring(path.length() - 1, path.length());
		if (!lastChar.equals(File.separator))
			path += File.separator;

		return path + getStoreFolderPath(ownerId, maxOwnerIdLength, fragmetLegth);
	}

	/**
	 * Return store file path with file name for given owner id.
	 * 
	 * @param ownerId
	 *            - owner id
	 * @param maxOwnerIdLength
	 *            - max owner id length
	 * @param fragmentLength
	 *            - fragments length
	 * @param extension
	 *            - file extension
	 * @return storing file path with file name
	 * @throws FSServiceConfigException
	 */
	public static final String getStoreFilePath(String ownerId, int maxOwnerIdLength, int fragmentLength, String aFileExtension)
			throws FSServiceConfigException {
		return getStoreFolderPath(ownerId, maxOwnerIdLength, fragmentLength) + getStoreFileName(ownerId, aFileExtension);
	}

	/**
	 * Return store file path with file name for given owner id.
	 * 
	 * @param ownerId
	 *            - owner id
	 * @return storing file path with file name
	 * @throws FSServiceConfigException
	 */
	public String getStoreFilePath(String ownerId) throws FSServiceConfigException {
		return getStoreFolderPath(ownerId) + getStoreFileName(ownerId);
	}

	/**
	 * Validation method.
	 * 
	 * @param ownerId
	 *            - owner id
	 * @return validated owner id
	 * @throws FSServiceConfigException
	 */
	private static String validateOwnerId(String ownerId) throws FSServiceConfigException {
		if (ownerId == null)
			throw new FSServiceConfigException(VALIDATION_ERROR_PREFIX + "Null ownerId argument.");

		if (ownerId.length() < 1)
			throw new FSServiceConfigException(VALIDATION_ERROR_PREFIX + "Minimum length for ownerId: 1.");

		try {
			return Integer.valueOf(ownerId).toString();
		} catch (NumberFormatException nfe) {
			throw new FSServiceConfigException(VALIDATION_ERROR_PREFIX + "NumberFormatException on parsing ownerId argument: " + nfe.getMessage());
		}
	}

	/**
	 * Validation method.
	 * 
	 * @param aRootFolderPath
	 *            - root folder path
	 * @return a root folder path
	 * @throws FSServiceConfigException
	 */
	private static String validateRootFilderPath(String aRootFolderPath) throws FSServiceConfigException {
		if (aRootFolderPath == null)
			throw new FSServiceConfigException(VALIDATION_ERROR_PREFIX + "Null aRootFolderPath argument.");

		return aRootFolderPath;
	}

	/**
	 * Validation method.
	 * 
	 * @param aFileExtension
	 *            - file extension
	 * @return file extension
	 * @throws FSServiceConfigException
	 */
	private static String validateFileExtension(String aFileExtension) throws FSServiceConfigException {
		if (aFileExtension == null)
			throw new FSServiceConfigException(VALIDATION_ERROR_PREFIX + "Null aServiceName argument.");

		return aFileExtension;
	}

}
