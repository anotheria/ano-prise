package net.anotheria.anoprise.dataspace.persistence;

/**
 * Dataspace persistence exception used in DataspacePersistenceService. Throwed if dataspace not found in persistence.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class DataspaceNotFoundException extends DataspacePersistenceServiceException {

	/**
	 * Basic serialVersionUID variable.
	 */
	private static final long serialVersionUID = 4675725133571302930L;

	/**
	 * Public constructor.
	 *
	 * @param userId
	 *            - user id
	 * @param dataspaceId
	 *            - dataspace id
	 */
	public DataspaceNotFoundException(String userId, String dataspaceId) {
		super("No dataspace found for user: " + userId + ", dataspace: " + dataspaceId);
	}
}
