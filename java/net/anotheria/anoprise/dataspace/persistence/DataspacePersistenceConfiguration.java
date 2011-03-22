package net.anotheria.anoprise.dataspace.persistence;

import org.apache.log4j.Logger;
import org.configureme.ConfigurationManager;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;

/**
 * {@link DataspacePersistenceService} configuration.
 * 
 * @author Alexandr Bolbat
 */
@ConfigureMe(name = "ano-prise-dataspace-config")
public class DataspacePersistenceConfiguration {

	/**
	 * Logger.
	 */
	private static final Logger LOGGER = Logger.getLogger(DataspacePersistenceConfiguration.class.getName());

	/**
	 * Field separator for queries.
	 */
	public static final String SEPARATOR = ", ";

	/**
	 * Table field user id name.
	 */
	public static final String DATASPACE_TABLE_FIELD_NAME_USER_ID = "userId";

	/**
	 * Table field dataspace id name.
	 */
	public static final String DATASPACE_TABLE_FIELD_NAME_DATASPACE_ID = "dataspaceId";

	/**
	 * Table field attributeName name.
	 */
	public static final String DATASPACE_TABLE_FIELD_NAME_ATTR_NAME = "attrName";

	/**
	 * Table field attributeTypeId name.
	 */
	public static final String DATASPACE_TABLE_FIELD_NAME_ATTR_TYPE_ID = "attrTypeId";

	/**
	 * Table field attributeValue name.
	 */
	public static final String DATASPACE_TABLE_FIELD_NAME_ATTR_VALUE = "attrValue";

	/**
	 * Table field updated name.
	 */
	public static final String DATASPACE_TABLE_FIELD_NAME_UPDATED = "updated";

	/**
	 * Configuration instance.
	 */
	private static DataspacePersistenceConfiguration INSTANCE;

	/**
	 * Database table name.
	 */
	@Configure
	private String tableName = "dataspace";

	/**
	 * Database owner name.
	 */
	@Configure
	private String dbOwnerName = "postgres";

	/**
	 * Table primary key name.
	 */
	public final String DATASPACE_TABLE_PK_NAME = getTableName() + "_pk";

	/**
	 * Get configuration instance.
	 * 
	 * @return {@link DataspacePersistenceConfiguration}
	 */
	public static synchronized DataspacePersistenceConfiguration getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new DataspacePersistenceConfiguration();

			try {
				ConfigurationManager.INSTANCE.configure(INSTANCE);
			} catch (Exception e) {
				LOGGER.error("getInstance() Configuration failed. Configuring with defaults.", e);
			}
		}

		return INSTANCE;
	}

	/**
	 * Get configuration instance configured from custom file.
	 * 
	 * @return {@link DataspacePersistenceConfiguration}
	 */
	public static DataspacePersistenceConfiguration getInstance(String configurationFileName) {
		DataspacePersistenceConfiguration configuration = new DataspacePersistenceConfiguration();

		try {
			ConfigurationManager.INSTANCE.configureAs(INSTANCE, configurationFileName);
		} catch (Exception e) {
			LOGGER.error("getInstance(" + configurationFileName + ") Configuration failed. Configuring with defaults.", e);
		}

		return configuration;
	}

	public void setTableName(String aTableName) {
		this.tableName = aTableName;
	}

	public String getTableName() {
		return tableName;
	}

	public String getDbOwnerName() {
		return dbOwnerName;
	}

	public void setDbOwnerName(String aDbOwnerName) {
		this.dbOwnerName = aDbOwnerName;
	}
	
	/**
	 * All fields separated by SEPARATOR.
	 */
	public final String DATASPACE_TABLE_FIELDS = 
			DATASPACE_TABLE_FIELD_NAME_USER_ID + SEPARATOR +
			DATASPACE_TABLE_FIELD_NAME_DATASPACE_ID + SEPARATOR +
			DATASPACE_TABLE_FIELD_NAME_ATTR_NAME + SEPARATOR +
			DATASPACE_TABLE_FIELD_NAME_ATTR_TYPE_ID + SEPARATOR +
			DATASPACE_TABLE_FIELD_NAME_ATTR_VALUE + SEPARATOR +
			DATASPACE_TABLE_FIELD_NAME_UPDATED;
	
	/**
	 * SQL create table.
	 */
	public final String SQL_META_CREATE_TABLE = "CREATE TABLE " + getTableName() + " (" +
			DATASPACE_TABLE_FIELD_NAME_USER_ID + " character varying NOT NULL, " +
			DATASPACE_TABLE_FIELD_NAME_DATASPACE_ID + " integer NOT NULL, " +
			DATASPACE_TABLE_FIELD_NAME_ATTR_NAME + " character varying NOT NULL, " +
			DATASPACE_TABLE_FIELD_NAME_ATTR_TYPE_ID + " integer NOT NULL, " +
			DATASPACE_TABLE_FIELD_NAME_ATTR_VALUE + " character varying NOT NULL, " +
			DATASPACE_TABLE_FIELD_NAME_UPDATED + " bigint NOT NULL, " +
			"CONSTRAINT " + DATASPACE_TABLE_PK_NAME + " PRIMARY KEY (" + 
				DATASPACE_TABLE_FIELD_NAME_USER_ID + ", " + 
				DATASPACE_TABLE_FIELD_NAME_DATASPACE_ID + ", " +
				DATASPACE_TABLE_FIELD_NAME_ATTR_NAME + ")" +
			");";	
	
	/**
	 * SQL set table owner.
	 */
	public final String SQL_META_SET_OWNER = "ALTER TABLE " + getTableName() + " OWNER TO " + getDbOwnerName() + ";";
	
	/**
	 * SQL get dataspace by userId and dataspaceId.
	 */
	public final String SQL_GET_DATASPACE_1 = 
			"SELECT " + DATASPACE_TABLE_FIELDS +
			" FROM " + getTableName() +
			" WHERE " + DATASPACE_TABLE_FIELD_NAME_USER_ID + " = ?" +
			" AND " + DATASPACE_TABLE_FIELD_NAME_DATASPACE_ID + "= ?;";
	
	/**
	 * SQL insert attribute.
	 */
	public final String SQL_INSERT_ATTRIBUTE_1 = 
			"INSERT INTO " + getTableName() +
			" (" + DATASPACE_TABLE_FIELDS + ")" +
			" VALUES (?, ?, ?, ?, ?, ?);";
	
	/**
	 * SQL remove dataspace by userId and dataspaceId.
	 */
	public final String SQL_REMOVE_DATASPACE_1 = 
			"DELETE FROM " + getTableName() +
			" WHERE " + DATASPACE_TABLE_FIELD_NAME_USER_ID + " = ?" +
			" AND " + DATASPACE_TABLE_FIELD_NAME_DATASPACE_ID + " = ?;";
	
	@Override
	public String toString() {
		return "DataspacePersistenceConfiguration [tableName=" + tableName + ", dbOwnerName=" + dbOwnerName + "]";
	}
	
}
