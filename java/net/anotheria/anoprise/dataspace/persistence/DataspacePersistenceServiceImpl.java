package net.anotheria.anoprise.dataspace.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.anotheria.anoprise.dataspace.Dataspace;
import net.anotheria.anoprise.dataspace.DataspaceType;
import net.anotheria.anoprise.dataspace.attribute.Attribute;
import net.anotheria.db.service.GenericPersistenceService;

import org.apache.log4j.Logger;

/**
 * DataspacePersistenceService implementation.
 * 
 * @author abolbat
 */
public class DataspacePersistenceServiceImpl extends GenericPersistenceService implements DataspacePersistenceService {

	/**
	 * Logger.
	 */
	private final Logger log = Logger.getLogger(DataspacePersistenceServiceImpl.class);

	/**
	 * String prefix for logging.
	 */
	private static final String LOG_PREFIX = "DATASPACE PERSISTENCE SERVICE: ";

	/**
	 * DDL queries.
	 */
	private static final List<String> ddlQueries = new ArrayList<String>();

	/**
	 * Static initialization of DDL queries by SQL commands.
	 */
	static {
		// table creation
		ddlQueries.add(DataspacePersistenceConstants.SQL_META_CREATE_TABLE);
		// grant privileges
		ddlQueries.add(DataspacePersistenceConstants.SQL_META_SET_OWNER);
	}

	@Override
	public Dataspace loadDataspace(String userId, DataspaceType dataspaceType) throws DataspacePersistenceServiceException {
		Dataspace result = new Dataspace(userId, dataspaceType);

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			conn = getConnection();
			st = conn.prepareStatement(DataspacePersistenceConstants.SQL_GET_DATASPACE_1);
			st.setString(1, userId);
			st.setInt(2, dataspaceType.getId());
			rs = st.executeQuery();

			while (rs.next()) {
				int attrType = rs.getInt(DataspacePersistenceConstants.DATASPACE_TABLE_FIELD_NAME_ATTR_TYPE_ID);
				String attrName = rs.getString(DataspacePersistenceConstants.DATASPACE_TABLE_FIELD_NAME_ATTR_NAME);
				String attrValue = rs.getString(DataspacePersistenceConstants.DATASPACE_TABLE_FIELD_NAME_ATTR_VALUE);

				result.addAttribute(attrName, Attribute.createAttribute(attrType, attrName, attrValue));
			}
		} catch (SQLException sqle) {
			log.error(LOG_PREFIX + "SQL Exception: " + sqle.getMessage());
			throw new DataspacePersistenceServiceException(sqle.getMessage());
		} finally {
			close(rs);
			close(st);
			close(conn);
		}

		return result;
	}

	@Override
	public void saveDataspace(Dataspace dataspace) throws DataspacePersistenceServiceException {
		Connection conn = null;
		PreparedStatement st = null;
		PreparedStatement st2 = null;
		ResultSet rs = null;

		try {
			conn = getConnection();
			conn.setAutoCommit(false);
			// remove old dataspace from persistence
			st = conn.prepareStatement(DataspacePersistenceConstants.SQL_REMOVE_DATASPACE_1);
			st.setString(1, dataspace.getUserId());
			st.setInt(2, dataspace.getDataspaceType().getId());
			st.executeUpdate();

			// create new dataspace in persistence
			st2 = conn.prepareStatement(DataspacePersistenceConstants.SQL_INSERT_ATTRIBUTE_1);

			if (conn.getMetaData().supportsBatchUpdates()) {
				for (Attribute attribute : dataspace.getAttributes()) {
					st2.setString(1, dataspace.getUserId());
					st2.setInt(2, dataspace.getDataspaceType().getId());
					st2.setString(3, attribute.getName());
					st2.setInt(4, attribute.getType().getTypeId());
					st2.setString(5, attribute.getValueAsString());
					st2.setLong(6, System.currentTimeMillis());
					st2.addBatch();
				}
				st2.executeBatch();
			} else {
				for (Attribute attribute : dataspace.getAttributes()) {
					st2.clearParameters();
					st2.setString(1, dataspace.getUserId());
					st2.setInt(2, dataspace.getDataspaceType().getId());
					st2.setString(3, attribute.getName());
					st2.setInt(4, attribute.getType().getTypeId());
					st2.setString(5, attribute.getValueAsString());
					st2.setLong(6, System.currentTimeMillis());
					st2.executeUpdate();
				}
			}
			conn.commit();
		} catch (SQLException sqle) {
			log.error(LOG_PREFIX + "SQL Exception: " + sqle.getMessage());
			throw new DataspacePersistenceServiceException(sqle.getMessage());
		} finally {
			close(rs);
			close(st2);
			close(st);
			close(conn);
		}
	}

	@Override
	protected List<String> getDDL() {
		return ddlQueries;
	}

	@Override
	protected String getTableName() {
		return DataspacePersistenceConstants.DATASPACE_TABLE_NAME;
	}

	@Override
	protected String getPKFieldName() {
		return null;
	}

}
