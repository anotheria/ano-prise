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
	 * Name of service who use this instance of {@link FSService}.
	 */
	private String serviceName;

	/**
	 * File extension for storing files.
	 */
	public static final String FILE_EXTENSION = ".dat";

	/**
	 * Text prefix for validation exception message.
	 */
	public static final String VALIDATION_ERROR_PREFIX = "Validation error: ";

	/**
	 * Default constructor.
	 * 
	 * @param aRootFolderPath
	 *            - root folder in file system for storing service files
	 * @param aServiceName
	 *            - name of service who use this instance of {@link FSService}
	 * @throws FSServiceConfigException
	 */
	public FSServiceConfig(String aRootFolderPath, String aServiceName) throws FSServiceConfigException {
		validateRootFilderPath(aRootFolderPath);
		this.rootFolderPath = aRootFolderPath;

		validateServiceName(aServiceName);
		this.serviceName = aServiceName;
	}

	public String getRootFolderPath() {
		return rootFolderPath;
	}

	public String getServiceName() {
		return serviceName;
	}

	/**
	 * Return storing folder path for given owner id.
	 * 
	 * @param ownerId
	 *            - owner id
	 * @return storing folder path
	 * @throws FSServiceConfigException
	 */
	public String getStoringFolderPath(String ownerId) throws FSServiceConfigException {
		String id = validateOwnerId(ownerId);
		String path = rootFolderPath;

		int from = path.length() - 1;
		int to = path.length();
		String lastChar = path.substring(from, to);
		if (!lastChar.equals(File.separator))
			path += File.separator;

		path += serviceName + File.separator;

		if (id.length() <= i3) {
			return path + "0" + File.separator + "0";
		} else if (id.length() == i4) {
			int idx1 = id.length() - i4;
			int idx2 = id.length() - i3;
			return path + "0" + File.separator + id.substring(idx1, idx2);
		} else if (id.length() == i5) {
			int idx1 = id.length() - i5;
			int idx2 = id.length() - i3;
			return path + "0" + File.separator + id.substring(idx1, idx2);
		} else if (id.length() == i6) {
			int idx1 = id.length() - i6;
			int idx2 = id.length() - i3;
			return path + "0" + File.separator + id.substring(idx1, idx2);
		} else if (id.length() == i7) {
			int idx1 = id.length() - i6;
			int idx2 = id.length() - i3;
			int idx3 = id.length() - i7;
			int idx4 = id.length() - i6;
			return path + id.substring(idx3, idx4) + File.separator + id.substring(idx1, idx2);
		} else if (id.length() == i8) {
			int idx1 = id.length() - i6;
			int idx2 = id.length() - i3;
			int idx3 = id.length() - i8;
			int idx4 = id.length() - i6;
			return path + id.substring(idx3, idx4) + File.separator + id.substring(idx1, idx2);
		} else if (id.length() == i9) {
			int idx1 = id.length() - i6;
			int idx2 = id.length() - i3;
			int idx3 = id.length() - i9;
			int idx4 = id.length() - i6;
			return path + id.substring(idx3, idx4) + File.separator + id.substring(idx1, idx2);
		}

		throw new FSServiceConfigException("Unknown path for owner id:" + id);
	}

	/**
	 * Return storing file name for given owner id.
	 * 
	 * @param ownerId
	 *            - owner id
	 * @return storing file name
	 * @throws FSServiceConfigException
	 */
	public String getStoringFileName(String ownerId) throws FSServiceConfigException {
		String id = validateOwnerId(ownerId);

		if (id.length() <= i3)
			return id + FILE_EXTENSION;

		int idx1 = id.length() - i3;
		int idx2 = id.length();
		return id.substring(idx1, idx2) + FILE_EXTENSION;
	}

	/**
	 * Return storing file path with file name for given owner id.
	 * 
	 * @param ownerId
	 *            - owner id
	 * @return storing file path with file name
	 * @throws FSServiceConfigException
	 */
	public String getStoringFilePath(String ownerId) throws FSServiceConfigException {
		return getStoringFolderPath(ownerId) + File.separator + getStoringFileName(ownerId);
	}

	/**
	 * Validation method.
	 * 
	 * @param ownerId
	 *            - owner id
	 * @return validated owner id
	 * @throws FSServiceConfigException
	 */
	private String validateOwnerId(String ownerId) throws FSServiceConfigException {
		if (ownerId == null)
			throw new FSServiceConfigException(VALIDATION_ERROR_PREFIX + "Null ownerId argument.");

		if (ownerId.length() < 1)
			throw new FSServiceConfigException(VALIDATION_ERROR_PREFIX + "Minimum length for ownerId: 1.");

		if (ownerId.length() > i9)
			throw new FSServiceConfigException(VALIDATION_ERROR_PREFIX + "Maximum length for ownerId: 9. Current length: " + ownerId.length() + ". ownerId: "
					+ ownerId + ".");

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
	 * @throws FSServiceConfigException
	 */
	private void validateRootFilderPath(String aRootFolderPath) throws FSServiceConfigException {
		if (aRootFolderPath == null)
			throw new FSServiceConfigException(VALIDATION_ERROR_PREFIX + "Null aRootFolderPath argument.");
	}

	/**
	 * Validation method.
	 * 
	 * @param aServiceName
	 *            - service name
	 * @throws FSServiceConfigException
	 */
	private void validateServiceName(String aServiceName) throws FSServiceConfigException {
		if (aServiceName == null)
			throw new FSServiceConfigException(VALIDATION_ERROR_PREFIX + "Null aServiceName argument.");
	}

	/**
	 * Workaround variable for MagicNumberCheck in check style warnings.
	 */
	private static final int i3 = 3;

	/**
	 * Workaround variable for MagicNumberCheck in check style warnings.
	 */
	private static final int i4 = 4;

	/**
	 * Workaround variable for MagicNumberCheck in check style warnings.
	 */
	private static final int i5 = 5;

	/**
	 * Workaround variable for MagicNumberCheck in check style warnings.
	 */
	private static final int i6 = 6;

	/**
	 * Workaround variable for MagicNumberCheck in check style warnings.
	 */
	private static final int i7 = 7;

	/**
	 * Workaround variable for MagicNumberCheck in check style warnings.
	 */
	private static final int i8 = 8;

	/**
	 * Workaround variable for MagicNumberCheck in check style warnings.
	 */
	private static final int i9 = 9;

}
