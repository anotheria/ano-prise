package net.anotheria.anoprise.dataspace.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.anotheria.anoprise.dataspace.Dataspace;
import net.anotheria.anoprise.dataspace.DataspaceType;
import net.anotheria.anoprise.dataspace.attribute.Attribute;
import net.anotheria.db.service.BasePersistenceServiceJDBCImpl;

import org.apache.log4j.Logger;

/**
 * DataspacePersistenceService implementation.
 * 
 * @author abolbat
 */
public class DataspacePersistenceServiceImpl extends BasePersistenceServiceJDBCImpl implements DataspacePersistenceService {

	/**
	 * Logger.
	 */
	private final Logger log = Logger.getLogger(DataspacePersistenceServiceImpl.class);

	/**
	 * String prefix for logging.
	 */
	private static final String LOG_PREFIX = "DATASPACE PERSISTENCE SERVICE: ";

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
			st.setInt(2, dataspaceType.getTypeId());
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
			st.setInt(2, dataspace.getDataspaceType().getTypeId());
			st.executeUpdate();

			// create new dataspace in persistence
			st2 = conn.prepareStatement(DataspacePersistenceConstants.SQL_INSERT_ATTRIBUTE_1);
			// workaround for checkstyle warnings
			final int f1 = 1;
			final int f2 = 2;
			final int f3 = 3;
			final int f4 = 4;
			final int f5 = 5;
			final int f6 = 6;
			// workaround end
			if (conn.getMetaData().supportsBatchUpdates()) {
				for (Attribute attribute : dataspace.getAttributes()) {
					st2.setString(f1, dataspace.getUserId());
					st2.setInt(f2, dataspace.getDataspaceType().getTypeId());
					st2.setString(f3, attribute.getName());
					st2.setInt(f4, attribute.getType().getTypeId());
					st2.setString(f5, attribute.getValueAsString());
					st2.setLong(f6, System.currentTimeMillis());
					st2.addBatch();
				}
				st2.executeBatch();
			} else {
				for (Attribute attribute : dataspace.getAttributes()) {
					st2.clearParameters();
					st2.setString(f1, dataspace.getUserId());
					st2.setInt(f2, dataspace.getDataspaceType().getTypeId());
					st2.setString(f3, attribute.getName());
					st2.setInt(f4, attribute.getType().getTypeId());
					st2.setString(f5, attribute.getValueAsString());
					st2.setLong(f6, System.currentTimeMillis());
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
	public void createPersistenceStructure() throws DataspacePersistenceServiceException {
		log.info(LOG_PREFIX + "Start creating persistence structure.");
		Connection conn = null;
		Statement st = null;
		try {
			conn = getConnection();
			st = conn.createStatement();
			log.info(LOG_PREFIX + "Creating table.");
			st.execute(DataspacePersistenceConstants.SQL_META_CREATE_TABLE);
			log.info(LOG_PREFIX + "Setting table owner.");
			st.execute(DataspacePersistenceConstants.SQL_META_SET_OWNER);
			log.info(LOG_PREFIX + "Structure created.");
		} catch (SQLException sqle) {
			log.error(LOG_PREFIX + "SQL Exception: " + sqle.getMessage());
			throw new DataspacePersistenceServiceException(sqle.getMessage());
		} finally {
			close(st);
			close(conn);
		}
	}

}
