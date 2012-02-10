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
import net.anotheria.util.concurrency.IdBasedLock;
import net.anotheria.util.concurrency.IdBasedLockManager;
import net.anotheria.util.concurrency.SafeIdBasedLockManager;

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
	 * DDL queries. Don't make this variable static.
	 */
	private final List<String> ddlQueries = new ArrayList<String>();

	/**
	 * Persistence service configuration
	 */
	private final DataspacePersistenceConfiguration configuration;

	/**
	 * {@link IdBasedLockManager} instance.
	 */
	private IdBasedLockManager lockManager;

	/**
	 * Default constructor.
	 * 
	 * @param aConfiguration
	 *            - persistence service configuration
	 */
	protected DataspacePersistenceServiceImpl(DataspacePersistenceConfiguration aConfiguration) {
		if (aConfiguration == null)
			throw new IllegalArgumentException("DataspacePersistenceServiceImpl(aConfiguration) fail. Null argument.");

		this.configuration = aConfiguration;

		// table creation
		ddlQueries.add(configuration.getDDLCreateTable());
		// grant privileges
		ddlQueries.add(configuration.getDDLSetOwner());

		initialize();

		lockManager = new SafeIdBasedLockManager();
	}

	@Override
	public Dataspace loadDataspace(String userId, DataspaceType dataspaceType) throws DataspacePersistenceServiceException {
		if (userId == null)
			throw new IllegalArgumentException("User id null");
		if (dataspaceType == null)
			throw new IllegalArgumentException("Dataspace type null");

		Dataspace result = new Dataspace(userId, dataspaceType);

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			conn = getConnection();
			st = conn.prepareStatement(configuration.getSQLGetDataspace());
			st.setString(1, userId);
			st.setInt(2, dataspaceType.getId());
			rs = st.executeQuery();

			while (rs.next()) {
				int attrType = rs.getInt(configuration.getFieldNameAttributeTypeId());
				String attrName = rs.getString(configuration.getFieldNameAttributeName());
				String attrValue = rs.getString(configuration.getFieldNameAttributeValue());

				result.addAttribute(attrName, Attribute.createAttribute(attrType, attrName, attrValue));
			}
		} catch (SQLException sqle) {
			log.error(LOG_PREFIX + "SQL Exception: " + sqle.getMessage(), sqle);
			throw new DataspacePersistenceServiceException(sqle.getMessage(), sqle);
		} finally {
			close(rs);
			close(st);
			close(conn);
		}

		return result;
	}

	@Override
	public void saveDataspace(Dataspace dataspace) throws DataspacePersistenceServiceException {
		if (dataspace == null)
			throw new IllegalArgumentException("Dataspace null");
		if (dataspace.getUserId() == null)
			throw new IllegalArgumentException("User id null");
		if (dataspace.getDataspaceType() == null)
			throw new IllegalArgumentException("Dataspace type null");

		Connection conn = null;
		PreparedStatement st = null;
		PreparedStatement st2 = null;

		String lockId = dataspace.getUserId() + "_" + dataspace.getDataspaceType();
		IdBasedLock lock = lockManager.obtainLock(lockId);
		lock.lock();
		try {
			conn = getConnection();
			conn.setAutoCommit(false);
			// remove old dataspace from persistence
			st = conn.prepareStatement(configuration.getSQLRemoveDataspace());
			st.setString(1, dataspace.getUserId());
			st.setInt(2, dataspace.getDataspaceType().getId());
			st.executeUpdate();

			// create new dataspace in persistence
			st2 = conn.prepareStatement(configuration.getSQLInsertAttribute());

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
			try {
				conn.rollback();
			} catch (SQLException sqle2) {
				log.error(LOG_PREFIX + "SQL Exception on transaction rollback: " + sqle2.getMessage(), sqle2);
			}
			log.error(LOG_PREFIX + "SQL Exception: " + sqle.getMessage(), sqle);
			throw new DataspacePersistenceServiceException(sqle.getMessage(), sqle);
		} finally {
			close(st2);
			close(st);
			close(conn);
			lock.unlock();
		}
	}

	@Override
	protected List<String> getDDL() {
		return ddlQueries;
	}

	@Override
	protected String getTableName() {
		return configuration.getTableName();
	}

	@Override
	protected String getPKFieldName() {
		return null;
	}

}
