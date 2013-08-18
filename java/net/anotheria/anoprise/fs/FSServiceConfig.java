package net.anotheria.anoprise.fs;

import org.configureme.ConfigurationManager;
import org.configureme.Environment;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.configureme.annotations.DontConfigure;
import org.configureme.environments.DynamicEnvironment;
import org.configureme.sources.ConfigurationSourceKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Interface of the file system service.
 *
 * @author abolbat
 * @version 1.0, 2010/02/11
 */
@ConfigureMe
public final class FSServiceConfig implements Serializable {
	/**
	 * File extension for storing files.
	 */
	@DontConfigure
	public static final String DEFAULT_CONFIG_NAME = "defaultFSConfig";
	/**
	 * File extension for storing files.
	 */
	@DontConfigure
	public static final String DEFAULT_FILE_EXTENSION = "dat";
	/**
	 * Default maximum owner id length.
	 */
	@DontConfigure
	public static final int DEFAULT_MAX_OWNER_ID_LENGTH = 10;
	/**
	 * Default fragment length.
	 */
	@DontConfigure
	public static final int DEFAULT_FRAGMENT_LENGTH = 2;
	/**
	 * Text prefix for validation exception message.
	 */
	@DontConfigure
	public static final String VALIDATION_ERROR_PREFIX = "Validation error: ";
	/**
	 * Basic serialVersionUID variable.
	 */
	@DontConfigure
	private static final long serialVersionUID = -2629878661534470687L;
	/**
	 * Synchronization object.
	 */
	@DontConfigure
	private static final Object LOCK = new Object();
	/**
	 * Configurations cache.
	 */
	@DontConfigure
	private static final Map<String, FSServiceConfig> CACHE = new HashMap<String, FSServiceConfig>();
	/**
	 * Logger.
	 */
	@DontConfigure
	private static Logger LOGGER = LoggerFactory.getLogger(FSServiceConfig.class);
	/**
	 * Root folder in file system for storing service files.
	 */
	@Configure
	private String rootFolderPath;
	/**
	 * Configurable file Extension.
	 */
	@Configure
	private String fileExtension;
	/**
	 * Maximum owner id length.
	 */
	@Configure
	private int maxOwnerIdLength;
	/**
	 * Fragment length.
	 */
	@Configure
	private int fragmetLegth;
	/**
	 * Allow to use any string as owner id, disabled by default.
	 */
	@Configure
	private boolean useStringOwnerId = false;

	/**
	 * Default constructor.
	 *
	 * @param configuration
	 * 		configuration name
	 * @param environment
	 * 		configuration environment
	 */
	private FSServiceConfig(final String configuration, final Environment environment) {
		try {
			if (configuration == null || configuration.trim().isEmpty()) {
				ConfigurationManager.INSTANCE.configure(this, environment);
			} else {
				ConfigurationManager.INSTANCE.configureAs(this, environment, configuration, ConfigurationSourceKey.Format.JSON);
			}
		} catch (RuntimeException e) {
			LOGGER.warn("FSServiceConfig(conf:" + configuration + ", env: " + environment + ") Configuration fail[" + e.getMessage()
					+ "]. Relaying on defaults.");
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("FSServiceConfig("+configuration+", "+environment+")", e);
		}
		if (fileExtension == null)
			fileExtension = DEFAULT_FILE_EXTENSION;
		if (maxOwnerIdLength == 0)
			maxOwnerIdLength = DEFAULT_MAX_OWNER_ID_LENGTH;
		if (fragmetLegth == 0)
			fragmetLegth = DEFAULT_FRAGMENT_LENGTH;
		LOGGER.info("FSServiceConfig(conf:" + configuration + ", env: " + environment + ") Configured with[" + this.toString() + "]");
	}

	/**
	 * Default constructor.
	 *
	 * @param aRootFolderPath
	 * 		- root folder in file system for storing service files
	 * @throws FSServiceConfigException
	 */
	public FSServiceConfig(String aRootFolderPath) throws FSServiceConfigException {
		this(aRootFolderPath, DEFAULT_FILE_EXTENSION, DEFAULT_MAX_OWNER_ID_LENGTH, DEFAULT_FRAGMENT_LENGTH);
	}

	/**
	 * Default constructor.
	 *
	 * @param aRootFolderPath
	 * 		- root folder in file system for storing service files
	 * @param aFileExtension
	 * 		- file extension
	 * @throws FSServiceConfigException
	 */
	public FSServiceConfig(String aRootFolderPath, String aFileExtension) throws FSServiceConfigException {
		this(aRootFolderPath, aFileExtension, DEFAULT_MAX_OWNER_ID_LENGTH, DEFAULT_FRAGMENT_LENGTH);
	}

	/**
	 * Default constructor.
	 *
	 * @param aRootFolderPath
	 * 		- root folder in file system for storing service files
	 * @param aFileExtension
	 * 		- file extension
	 * @param aMaxOwnerIdLength
	 * 		- maximum owner id length
	 * @param aFragmentLength
	 * 		- a fragment length
	 * @throws FSServiceConfigException
	 */
	public FSServiceConfig(String aRootFolderPath, String aFileExtension, int aMaxOwnerIdLength, int aFragmentLength) throws FSServiceConfigException {
		this.rootFolderPath = validateRootFolderPath(aRootFolderPath);
		this.fileExtension = validateFileExtension(aFileExtension);
		this.maxOwnerIdLength = aMaxOwnerIdLength;
		this.fragmetLegth = aFragmentLength;
		this.useStringOwnerId = false;
	}

	/**
	 * Default constructor.
	 *
	 * @param aRootFolderPath
	 * 		- root folder in file system for storing service files
	 * @param aFileExtension
	 * 		- file extension
	 * @param aMaxOwnerIdLength
	 * 		- maximum owner id length
	 * @param aFragmentLength
	 * 		- a fragment length
	 * @param stringOwnerId
	 * 		- allow string as owner id
	 * @throws FSServiceConfigException
	 */
	public FSServiceConfig(String aRootFolderPath, String aFileExtension, int aMaxOwnerIdLength, int aFragmentLength, boolean stringOwnerId) throws FSServiceConfigException {
		this.rootFolderPath = validateRootFolderPath(aRootFolderPath);
		this.fileExtension = validateFileExtension(aFileExtension);
		this.maxOwnerIdLength = aMaxOwnerIdLength;
		this.fragmetLegth = aFragmentLength;
		this.useStringOwnerId = stringOwnerId;

	}

	/**
	 * Get configured instance of {@link FSServiceConfig}.
	 *
	 * @param configuration
	 * 		configuration name, can be <code>null</code> or empty
	 * @param environment
	 * 		environment name, can be <code>null</code> or empty
	 * @return {@link FSServiceConfig}
	 */
	public static FSServiceConfig getInstance(final String configuration, final String environment) {
		String configName = String.valueOf(configuration) + "/-/" + String.valueOf(environment);
		if ((configuration == null || configuration.trim().isEmpty()) && (environment == null || environment.trim().isEmpty()))
			configName = DEFAULT_CONFIG_NAME;

		FSServiceConfig config = CACHE.get(configName);
		if (config != null)
			return config;

		Environment env = ConfigurationManager.INSTANCE.getDefaultEnvironment();
		if (environment != null && !environment.trim().isEmpty())
			env = DynamicEnvironment.parse(environment);

		synchronized (LOCK) {
			config = CACHE.get(configName);
			if (config == null)
				config = new FSServiceConfig(configuration, env);
			CACHE.put(configName, config);
		}

		return config;
	}

	/**
	 * Get configured instance of {@link FSServiceConfig}.
	 *
	 * @return {@link FSServiceConfig}
	 */
	public static FSServiceConfig getInstance() {
		return getInstance(null, null);
	}

	/**
	 * Get configured instance of {@link FSServiceConfig}.
	 *
	 * @param configuration
	 * 		configuration name, can be <code>null</code> or empty
	 * @return {@link FSServiceConfig}
	 */
	public static FSServiceConfig getInstance(final String configuration) {
		return getInstance(configuration, null);
	}

	/**
	 * Return store file name.
	 *
	 * @param ownerId
	 * 		- owner id
	 * @param aFileExtension
	 * 		- file extension
	 * @param useStringOwnerId
	 * 		- {@code true} if user id represented as string/ false  if it's int
	 * @return file name
	 * @throws FSServiceConfigException
	 */
	public static String getStoreFileName(String ownerId, String aFileExtension, boolean useStringOwnerId) throws FSServiceConfigException {
		return validateOwnerId(ownerId, useStringOwnerId) + "." + aFileExtension;
	}

	/**
	 * Internal method for fragmenting owner id by parameters.
	 *
	 * @param ownerId
	 * 		- owner id
	 * @param maxOwnerIdLength
	 * 		- max owner id length
	 * @param fragmentLength
	 * 		- fragment length
	 * @return fragments
	 */
	private static String[] fragmentOwnerId(String ownerId, int maxOwnerIdLength, int fragmentLength) {
		if (ownerId == null || ownerId.length() == 0)
			throw new IllegalArgumentException("OwnerId is null or empty");

		while (ownerId.length() < maxOwnerIdLength)
			ownerId = "0" + ownerId;

		while (ownerId.length() % fragmentLength != 0)
			ownerId = "0" + ownerId;

		int fragmentationDepth = ownerId.length() / fragmentLength;
		String[] ret = new String[fragmentationDepth - 1];
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
	 * 		- owner id
	 * @param maxOwnerIdLength
	 * 		- max owner id length
	 * @param fragmentLength
	 * 		- fragments length
	 * @param useStringOwnerId
	 * 		- is user id represented as string
	 * @return folder name
	 * @throws FSServiceConfigException
	 */
	public static String getStoreFolderPath(String ownerId, int maxOwnerIdLength, int fragmentLength, boolean useStringOwnerId) throws FSServiceConfigException {
		String id = validateOwnerId(ownerId, useStringOwnerId);
		String[] fragments = fragmentOwnerId(id, maxOwnerIdLength, fragmentLength);
		StringBuilder ret = new StringBuilder();
		for (String f : fragments) {
			ret.append(f).append(File.separatorChar);
		}
		return ret.toString();
	}

	/**
	 * Return store file path with file name for given owner id.
	 *
	 * @param ownerId
	 * 		- owner id
	 * @param maxOwnerIdLength
	 * 		- max owner id length
	 * @param fragmentLength
	 * 		- fragments length
	 * @param aFileExtension
	 * 		- file extension
	 * @param useStringOwnerId
	 * 		- is user id represented as string
	 * @return storing file path with file name
	 * @throws FSServiceConfigException
	 */
	public static String getStoreFilePath(String ownerId, int maxOwnerIdLength, int fragmentLength, String aFileExtension, boolean useStringOwnerId) throws FSServiceConfigException {
		return getStoreFolderPath(ownerId, maxOwnerIdLength, fragmentLength, useStringOwnerId) + getStoreFileName(ownerId, aFileExtension, useStringOwnerId);
	}

	/**
	 * Validation method.
	 *
	 * @param ownerId
	 * 		- owner id
	 * @param useStringOwnerId
	 * 		- allow/disalow user id as String usage
	 * @return validated owner id
	 * @throws FSServiceConfigException
	 */
	private static String validateOwnerId(String ownerId, boolean useStringOwnerId) throws FSServiceConfigException {
		if (ownerId == null)
			throw new FSServiceConfigException(VALIDATION_ERROR_PREFIX + "Null ownerId argument.");

		if (ownerId.length() < 1)
			throw new FSServiceConfigException(VALIDATION_ERROR_PREFIX + "Minimum length for ownerId: 1.");

		try {
			if (!useStringOwnerId)
				return Integer.valueOf(ownerId).toString();
			return ownerId;
		} catch (NumberFormatException nfe) {
			throw new FSServiceConfigException(VALIDATION_ERROR_PREFIX + "NumberFormatException on parsing ownerId argument: " + nfe.getMessage());
		}
	}

	/**
	 * Validation method.
	 *
	 * @param aRootFolderPath
	 * 		- root folder path
	 * @return a root folder path
	 * @throws FSServiceConfigException
	 */
	private static String validateRootFolderPath(String aRootFolderPath) throws FSServiceConfigException {
		if (aRootFolderPath == null)
			throw new FSServiceConfigException(VALIDATION_ERROR_PREFIX + "Null aRootFolderPath argument.");

		return aRootFolderPath;
	}

	/**
	 * Validation method.
	 *
	 * @param aFileExtension
	 * 		- file extension
	 * @return file extension
	 * @throws FSServiceConfigException
	 */
	private static String validateFileExtension(String aFileExtension) throws FSServiceConfigException {
		if (aFileExtension == null)
			throw new FSServiceConfigException(VALIDATION_ERROR_PREFIX + "Null aServiceName argument.");

		return aFileExtension;
	}

	public String getRootFolderPath() {
		return rootFolderPath;
	}

	public void setRootFolderPath(String rootFolderPath) {
		this.rootFolderPath = rootFolderPath;
	}

	public String getFileExtension() {
		return fileExtension;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	/**
	 * Return store file name.
	 *
	 * @param ownerId
	 * 		- owner id
	 * @return file name
	 * @throws FSServiceConfigException
	 */
	public String getStoreFileName(String ownerId) throws FSServiceConfigException {
		return getStoreFileName(ownerId, fileExtension, useStringOwnerId);
	}

	/**
	 * Return store folder path.
	 *
	 * @param ownerId
	 * 		- owner id
	 * @return folder name
	 * @throws FSServiceConfigException
	 */
	public String getStoreFolderPath(String ownerId) throws FSServiceConfigException {
		String path = rootFolderPath;
		String lastChar = path.substring(path.length() - 1, path.length());
		if (!lastChar.equals(File.separator))
			path += File.separator;

		return path + getStoreFolderPath(ownerId, maxOwnerIdLength, fragmetLegth, useStringOwnerId);
	}

	/**
	 * Return store file path with file name for given owner id.
	 *
	 * @param ownerId
	 * 		- owner id
	 * @return storing file path with file name
	 * @throws FSServiceConfigException
	 */
	public String getStoreFilePath(String ownerId) throws FSServiceConfigException {
		return getStoreFolderPath(ownerId) + getStoreFileName(ownerId);
	}

	public void setMaxOwnerIdLength(int maxOwnerIdLength) {
		this.maxOwnerIdLength = maxOwnerIdLength;
	}

	public void setFragmetLegth(int fragmetLegth) {
		this.fragmetLegth = fragmetLegth;
	}

	public void setUseStringOwnerId(boolean useStringOwnerId) {
		this.useStringOwnerId = useStringOwnerId;
	}
}
