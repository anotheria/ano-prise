package net.anotheria.anoprise.dataspace.persistence;

import org.configureme.ConfigurationManager;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.configureme.annotations.DontConfigure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link net.anotheria.anoprise.dataspace.persistence.DataspacePersistenceService} configuration.
 *
 * @author Alexandr Bolbat
 * @version $Id: $Id
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
	 * @return {@link net.anotheria.anoprise.dataspace.persistence.DataspacePersistenceConfiguration}
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
	 * @param configurationFileName
	 *            - file name
	 * @return {@link net.anotheria.anoprise.dataspace.persistence.DataspacePersistenceConfiguration}
	 */
	public static DataspacePersistenceConfiguration getInstance(String configurationFileName) {
		DataspacePersistenceConfiguration configuration = new DataspacePersistenceConfiguration();

		try {
			ConfigurationManager.INSTANCE.configureAs(configuration, configurationFileName);
		} catch (Exception e) {
			LOGGER.error("getInstance(" + configurationFileName + ") Configuration failed. Configuring with defaults.", e);
		}

		return configuration;
	}

	/**
	 * <p>Setter for the field <code>tableName</code>.</p>
	 *
	 * @param aTableName a {@link java.lang.String} object.
	 */
	public void setTableName(String aTableName) {
		this.tableName = aTableName;
	}

	/**
	 * <p>Getter for the field <code>tableName</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * <p>Getter for the field <code>dbOwnerName</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getDbOwnerName() {
		return dbOwnerName;
	}

	/**
	 * <p>Setter for the field <code>dbOwnerName</code>.</p>
	 *
	 * @param aDbOwnerName a {@link java.lang.String} object.
	 */
	public void setDbOwnerName(String aDbOwnerName) {
		this.dbOwnerName = aDbOwnerName;
	}

	/**
	 * <p>Setter for the field <code>primaryKeyName</code>.</p>
	 *
	 * @param aPrimaryKeyName a {@link java.lang.String} object.
	 */
	public void setPrimaryKeyName(String aPrimaryKeyName) {
		this.primaryKeyName = aPrimaryKeyName;
	}

	/**
	 * <p>Getter for the field <code>primaryKeyName</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getPrimaryKeyName() {
		return primaryKeyName;
	}

	/**
	 * <p>Setter for the field <code>fieldNameUserId</code>.</p>
	 *
	 * @param aFieldNameUserId a {@link java.lang.String} object.
	 */
	public void setFieldNameUserId(String aFieldNameUserId) {
		this.fieldNameUserId = aFieldNameUserId;
	}

	/**
	 * <p>Getter for the field <code>fieldNameUserId</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getFieldNameUserId() {
		return fieldNameUserId;
	}

	/**
	 * <p>Setter for the field <code>fieldNameDataspaceId</code>.</p>
	 *
	 * @param aFieldNameDataspaceId a {@link java.lang.String} object.
	 */
	public void setFieldNameDataspaceId(String aFieldNameDataspaceId) {
		this.fieldNameDataspaceId = aFieldNameDataspaceId;
	}

	/**
	 * <p>Getter for the field <code>fieldNameDataspaceId</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getFieldNameDataspaceId() {
		return fieldNameDataspaceId;
	}

	/**
	 * <p>Setter for the field <code>fieldNameAttributeName</code>.</p>
	 *
	 * @param aFieldNameAttributeName a {@link java.lang.String} object.
	 */
	public void setFieldNameAttributeName(String aFieldNameAttributeName) {
		this.fieldNameAttributeName = aFieldNameAttributeName;
	}

	/**
	 * <p>Getter for the field <code>fieldNameAttributeName</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getFieldNameAttributeName() {
		return fieldNameAttributeName;
	}

	/**
	 * <p>Setter for the field <code>fieldNameAttributeTypeId</code>.</p>
	 *
	 * @param aFieldNameAttributeTypeId a {@link java.lang.String} object.
	 */
	public void setFieldNameAttributeTypeId(String aFieldNameAttributeTypeId) {
		this.fieldNameAttributeTypeId = aFieldNameAttributeTypeId;
	}

	/**
	 * <p>Getter for the field <code>fieldNameAttributeTypeId</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getFieldNameAttributeTypeId() {
		return fieldNameAttributeTypeId;
	}

	/**
	 * <p>Setter for the field <code>fieldNameAttributeValue</code>.</p>
	 *
	 * @param aFieldNameAttributeValue a {@link java.lang.String} object.
	 */
	public void setFieldNameAttributeValue(String aFieldNameAttributeValue) {
		this.fieldNameAttributeValue = aFieldNameAttributeValue;
	}

	/**
	 * <p>Getter for the field <code>fieldNameAttributeValue</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getFieldNameAttributeValue() {
		return fieldNameAttributeValue;
	}

	/**
	 * <p>Setter for the field <code>fieldNameUpdated</code>.</p>
	 *
	 * @param aFieldNameUpdated a {@link java.lang.String} object.
	 */
	public void setFieldNameUpdated(String aFieldNameUpdated) {
		this.fieldNameUpdated = aFieldNameUpdated;
	}

	/**
	 * <p>Getter for the field <code>fieldNameUpdated</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getFieldNameUpdated() {
		return fieldNameUpdated;
	}

	/**
	 * All fields separated by SEPARATOR.
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getTableFields() {
		return getFieldNameUserId() + SEPARATOR + getFieldNameDataspaceId() + SEPARATOR + getFieldNameAttributeName() + SEPARATOR
				+ getFieldNameAttributeTypeId() + SEPARATOR + getFieldNameAttributeValue() + SEPARATOR + getFieldNameUpdated();
	}

	/**
	 * SQL create table.
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getDDLCreateTable() {
		return "CREATE TABLE " + getTableName() + " (" + getFieldNameUserId() + " character varying NOT NULL, " + getFieldNameDataspaceId()
				+ " integer NOT NULL, " + getFieldNameAttributeName() + " character varying NOT NULL, " + getFieldNameAttributeTypeId() + " integer NOT NULL, "
				+ getFieldNameAttributeValue() + " character varying NOT NULL, " + getFieldNameUpdated() + " bigint NOT NULL, " + "CONSTRAINT "
				+ getPrimaryKeyName() + " PRIMARY KEY (" + getFieldNameUserId() + ", " + getFieldNameDataspaceId() + ", " + getFieldNameAttributeName() + ")"
				+ ");";
	}

	/**
	 * SQL set table owner.
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getDDLSetOwner() {
		return "GRANT ALL ON " + getTableName() + " TO " + getDbOwnerName() + ";";
	}

	/**
	 * SQL get dataspace by userId and dataspaceId.
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getSQLGetDataspace() {
		return "SELECT " + getTableFields() + " FROM " + getTableName() + " WHERE " + getFieldNameUserId() + " = ?" + " AND " + getFieldNameDataspaceId()
				+ "= ?;";
	}

	/**
	 * SQL insert attribute.
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getSQLInsertAttribute() {
		return "INSERT INTO " + getTableName() + " (" + getTableFields() + ")" + " VALUES (?, ?, ?, ?, ?, ?);";
	}

	/**
	 * SQL remove dataspace by userId and dataspaceId.
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getSQLRemoveDataspace() {
		return "DELETE FROM " + getTableName() + " WHERE " + getFieldNameUserId() + " = ?" + " AND " + getFieldNameDataspaceId() + " = ?;";
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "DataspacePersistenceConfiguration [tableName=" + tableName + ", dbOwnerName=" + dbOwnerName + ", primaryKeyName=" + primaryKeyName
				+ ", fieldNameUserId=" + fieldNameUserId + ", fieldNameDataspaceId=" + fieldNameDataspaceId + ", fieldNameAttributeName="
				+ fieldNameAttributeName + ", fieldNameAttributeTypeId=" + fieldNameAttributeTypeId + ", fieldNameAttributeValue=" + fieldNameAttributeValue
				+ ", fieldNameUpdated=" + fieldNameUpdated + "]";
	}

}
