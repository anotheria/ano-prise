package net.anotheria.anoprise.dataspace.persistence;

import org.configureme.ConfigurationManager;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.configureme.annotations.DontConfigure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	@DontConfigure
	private static final Logger LOGGER = LoggerFactory.getLogger(DataspacePersistenceConfiguration.class.getName());

	/**
	 * Field separator for queries.
	 */
	@DontConfigure
	public static final String SEPARATOR = ", ";

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
	@Configure
	private String primaryKeyName = "dataspace_pk";

	/**
	 * Table field user id name.
	 */
	@Configure
	private String fieldNameUserId = "userId";

	/**
	 * Table field dataspace id name.
	 */
	@Configure
	private String fieldNameDataspaceId = "dataspaceId";

	/**
	 * Table field attributeName name.
	 */
	@Configure
	private String fieldNameAttributeName = "attrName";

	/**
	 * Table field attributeTypeId name.
	 */
	@Configure
	private String fieldNameAttributeTypeId = "attrTypeId";

	/**
	 * Table field attributeValue name.
	 */
	@Configure
	private String fieldNameAttributeValue = "attrValue";

	/**
	 * Table field updated name.
	 */
	@Configure
	private String fieldNameUpdated = "updated";

	/**
	 * Configuration instance.
	 */
	@DontConfigure
	private static DataspacePersistenceConfiguration INSTANCE;

	/**
	 * Get configuration instance.
	 *
	 * @return {@link DataspacePersistenceConfiguration}
	 */
	public static DataspacePersistenceConfiguration getInstance() {
		synchronized (DataspacePersistenceConfiguration.class) {
			if (INSTANCE == null) {
				INSTANCE = new DataspacePersistenceConfiguration();

				try {
					ConfigurationManager.INSTANCE.configure(INSTANCE);
				} catch (RuntimeException e) {
					LOGGER.error("getInstance() Configuration failed. Configuring with defaults.", e);
				}
			}

			return INSTANCE;
		}
	}

	/**
	 * Get configuration instance configured from custom file.
	 * 
	 * @param configurationFileName
	 *            - file name
	 * @return {@link DataspacePersistenceConfiguration}
	 */
	public static DataspacePersistenceConfiguration getInstance(String configurationFileName) {
		DataspacePersistenceConfiguration configuration = new DataspacePersistenceConfiguration();

		try {
			ConfigurationManager.INSTANCE.configureAs(configuration, configurationFileName);
		} catch (RuntimeException e) {
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

	public void setPrimaryKeyName(String aPrimaryKeyName) {
		this.primaryKeyName = aPrimaryKeyName;
	}

	public String getPrimaryKeyName() {
		return primaryKeyName;
	}

	public void setFieldNameUserId(String aFieldNameUserId) {
		this.fieldNameUserId = aFieldNameUserId;
	}

	public String getFieldNameUserId() {
		return fieldNameUserId;
	}

	public void setFieldNameDataspaceId(String aFieldNameDataspaceId) {
		this.fieldNameDataspaceId = aFieldNameDataspaceId;
	}

	public String getFieldNameDataspaceId() {
		return fieldNameDataspaceId;
	}

	public void setFieldNameAttributeName(String aFieldNameAttributeName) {
		this.fieldNameAttributeName = aFieldNameAttributeName;
	}

	public String getFieldNameAttributeName() {
		return fieldNameAttributeName;
	}

	public void setFieldNameAttributeTypeId(String aFieldNameAttributeTypeId) {
		this.fieldNameAttributeTypeId = aFieldNameAttributeTypeId;
	}

	public String getFieldNameAttributeTypeId() {
		return fieldNameAttributeTypeId;
	}

	public void setFieldNameAttributeValue(String aFieldNameAttributeValue) {
		this.fieldNameAttributeValue = aFieldNameAttributeValue;
	}

	public String getFieldNameAttributeValue() {
		return fieldNameAttributeValue;
	}

	public void setFieldNameUpdated(String aFieldNameUpdated) {
		this.fieldNameUpdated = aFieldNameUpdated;
	}

	public String getFieldNameUpdated() {
		return fieldNameUpdated;
	}

	/**
	 * All fields separated by SEPARATOR.
	 */
	public String getTableFields() {
        return fieldNameUserId + SEPARATOR + fieldNameDataspaceId + SEPARATOR + fieldNameAttributeName + SEPARATOR
				+ fieldNameAttributeTypeId + SEPARATOR + fieldNameAttributeValue + SEPARATOR + fieldNameUpdated;
	}

	/**
	 * SQL create table.
	 */
	public String getDDLCreateTable() {
        return "CREATE TABLE " + tableName + " (" + fieldNameUserId + " character varying NOT NULL, " + fieldNameDataspaceId
				+ " integer NOT NULL, " + fieldNameAttributeName + " character varying NOT NULL, " + fieldNameAttributeTypeId + " integer NOT NULL, "
				+ fieldNameAttributeValue + " character varying NOT NULL, " + fieldNameUpdated + " bigint NOT NULL, " + "CONSTRAINT "
				+ primaryKeyName + " PRIMARY KEY (" + fieldNameUserId + ", " + fieldNameDataspaceId + ", " + fieldNameAttributeName + ')'
				+ ");";
	}

	/**
	 * SQL set table owner.
	 */
	public String getDDLSetOwner() {
        return "GRANT ALL ON " + tableName + " TO " + dbOwnerName + ';';
	}

	/**
	 * SQL get dataspace by userId and dataspaceId.
	 */
	public String getSQLGetDataspace() {
        return "SELECT " + getTableFields() + " FROM " + tableName + " WHERE " + fieldNameUserId + " = ?" + " AND " + fieldNameDataspaceId
				+ "= ?;";
	}

	/**
	 * SQL insert attribute.
	 */
	public String getSQLInsertAttribute() {
        return "INSERT INTO " + tableName + " (" + getTableFields() + ')' + " VALUES (?, ?, ?, ?, ?, ?);";
	}

	/**
	 * SQL remove dataspace by userId and dataspaceId.
	 */
	public String getSQLRemoveDataspace() {
        return "DELETE FROM " + tableName + " WHERE " + fieldNameUserId + " = ?" + " AND " + fieldNameDataspaceId + " = ?;";
	}

	@Override
	public String toString() {
		return "DataspacePersistenceConfiguration [tableName=" + tableName + ", dbOwnerName=" + dbOwnerName + ", primaryKeyName=" + primaryKeyName
				+ ", fieldNameUserId=" + fieldNameUserId + ", fieldNameDataspaceId=" + fieldNameDataspaceId + ", fieldNameAttributeName="
				+ fieldNameAttributeName + ", fieldNameAttributeTypeId=" + fieldNameAttributeTypeId + ", fieldNameAttributeValue=" + fieldNameAttributeValue
				+ ", fieldNameUpdated=" + fieldNameUpdated + ']';
	}

}
