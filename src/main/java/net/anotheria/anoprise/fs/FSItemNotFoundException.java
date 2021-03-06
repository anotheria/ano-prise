package net.anotheria.anoprise.fs;

/**
 * FSItemNotFoundException exception of the {@link FSService} interface based on {@link FSServiceException}.
 * 
 * @author abolbat
 * @version 1.0, 2010/02/11
 */
public class FSItemNotFoundException extends FSServiceException {

	/**
	 * Basic serialVersionUID variable.
	 */
	private static final long serialVersionUID = 137192803182091660L;

	/**
	 * Creates a new {@link FSItemNotFoundException} with a item id.
	 * 
	 * @param ownerId
	 *            - owner id
	 */
	public FSItemNotFoundException(String ownerId) {
		super("File system item not found. Owner id: " + ownerId);
	}
}
