package net.anotheria.anoprise.dataspace.persistence;

/**
 * DataspacePersistenceService constant variables.
 * 
 * @author abolbat
 */
public final class DataspacePersistenceConstants {
	
	/**
	 * Field separator for queries.
	 */
	public static final String SEPARATOR = ", ";
	
	/**
	 * Table name.
	 */
	public static final String DATASPACE_TABLE_NAME = "dataspace";
	
	/**
	 * Table owner.
	 */
	public static final String DATASPACE_TABLE_OWNER = "affairo";
	
	/**
	 * Table primary key name.
	 */
	public static final String DATASPACE_TABLE_PK_NAME = "dataspace_pk";
	
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
	 * Table field updated  name.
	 */
	public static final String DATASPACE_TABLE_FIELD_NAME_UPDATED = "updated";
	
	/**
	 * All fields separated by SEPARATOR.
	 */
	public static final String DATASPACE_TABLE_FIELDS = 
			DATASPACE_TABLE_FIELD_NAME_USER_ID + SEPARATOR +
			DATASPACE_TABLE_FIELD_NAME_DATASPACE_ID + SEPARATOR +
			DATASPACE_TABLE_FIELD_NAME_ATTR_NAME + SEPARATOR +
			DATASPACE_TABLE_FIELD_NAME_ATTR_TYPE_ID + SEPARATOR +
			DATASPACE_TABLE_FIELD_NAME_ATTR_VALUE + SEPARATOR +
			DATASPACE_TABLE_FIELD_NAME_UPDATED;
	
	/**
	 * SQL create table.
	 */
	public static final String SQL_META_CREATE_TABLE = "CREATE TABLE " + DATASPACE_TABLE_NAME + " (" +
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
	public static final String SQL_META_SET_OWNER = "ALTER TABLE " + DATASPACE_TABLE_NAME + " OWNER TO " + DATASPACE_TABLE_OWNER + ";";
	
	/**
	 * SQL get dataspace by userId and dataspaceId.
	 */
	public static final String SQL_GET_DATASPACE_1 = 
			"SELECT " + DATASPACE_TABLE_FIELDS +
			" FROM " + DATASPACE_TABLE_NAME +
			" WHERE " + DATASPACE_TABLE_FIELD_NAME_USER_ID + " = ?" +
			" AND " + DATASPACE_TABLE_FIELD_NAME_DATASPACE_ID + "= ?;";
	
	/**
	 * SQL get attribute by userId, dataspaceId and attributeName.
	 */
	public static final String SQL_GET_ATTRIBUTE_1 = 
			"SELECT " + DATASPACE_TABLE_FIELDS +
			" FROM " + DATASPACE_TABLE_NAME +
			" WHERE " + DATASPACE_TABLE_FIELD_NAME_USER_ID + " = ?" +
			" AND " + DATASPACE_TABLE_FIELD_NAME_DATASPACE_ID + "= ?" +
			" AND " + DATASPACE_TABLE_FIELD_NAME_ATTR_NAME + "= ?;";
	
	/**
	 * SQL insert attribute.
	 */
	public static final String SQL_INSERT_ATTRIBUTE_1 = 
			"INSERT INTO " + DATASPACE_TABLE_NAME +
			" (" + DATASPACE_TABLE_FIELDS + ")" +
			" VALUES (?, ?, ?, ?, ?, ?);";
	
	/**
	 * SQL update attribute by userId, dataspaceId and attributeName.
	 */
	public static final String SQL_UPDATE_ATTRIBUTE_1 = 
			"UPDATE " + DATASPACE_TABLE_NAME +
			" SET " + DATASPACE_TABLE_FIELD_NAME_ATTR_TYPE_ID + " = ?, " +
			DATASPACE_TABLE_FIELD_NAME_ATTR_VALUE + " = ?, " +
			DATASPACE_TABLE_FIELD_NAME_UPDATED + " = ?" +
			" WHERE " + DATASPACE_TABLE_FIELD_NAME_USER_ID + " = ?" +
			" AND " + DATASPACE_TABLE_FIELD_NAME_DATASPACE_ID + " = ?" +
			" AND " + DATASPACE_TABLE_FIELD_NAME_ATTR_NAME + " = ?;";
	
	/**
	 * SQL remove dataspace by userId and dataspaceId.
	 */
	public static final String SQL_REMOVE_DATASPACE_1 = 
			"DELETE FROM " + DATASPACE_TABLE_NAME +
			" WHERE " + DATASPACE_TABLE_FIELD_NAME_USER_ID + " = ?" +
			" AND " + DATASPACE_TABLE_FIELD_NAME_DATASPACE_ID + " = ?;";
	
	/**
	 * SQL remove attribute by userId, dataspaceId and attributeName.
	 */
	public static final String SQL_REMOVE_ATRIBUTE_1 = 
			"DELETE FROM " + DATASPACE_TABLE_NAME +
			" WHERE " + DATASPACE_TABLE_FIELD_NAME_USER_ID + " = ?" +
			" AND " + DATASPACE_TABLE_FIELD_NAME_DATASPACE_ID + " = ?" +
			" AND " + DATASPACE_TABLE_FIELD_NAME_ATTR_NAME + " = ?;";
	
	/**
	 * Default constructor.
	 */
	private DataspacePersistenceConstants() {		
	}
}
