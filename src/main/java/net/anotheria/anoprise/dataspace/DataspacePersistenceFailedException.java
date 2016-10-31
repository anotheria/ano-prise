package net.anotheria.anoprise.dataspace;

import net.anotheria.anoprise.dataspace.persistence.DataspacePersistenceServiceException;

/**
 * Dataspace exception used in DataspaceService. Throwed on persistence fail.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class DataspacePersistenceFailedException extends DataspaceServiceException {

	/**
	 * Basic serialVersionUID variable.
	 */
	private static final long serialVersionUID = 3741636668816215786L;

	/**
	 * Public constructor.
	 *
	 * @param cause
	 *            - exception cause
	 */
	public DataspacePersistenceFailedException(DataspacePersistenceServiceException cause) {
		super("Dataspace Persistence Failed: " + cause.getMessage(), cause);
	}
}
