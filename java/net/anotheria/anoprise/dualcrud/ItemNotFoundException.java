package net.anotheria.anoprise.dualcrud;

/**
 * ItemNotFoundException exception of the {@link CrudService} interface based on {@link CrudServiceException}.
 * 
 * @author lrosenberg
 * @version 1.0, 2010/02/02
 */
public class ItemNotFoundException extends CrudServiceException {

	/**
	 * Basic serialVersionUID variable.
	 */
	private static final long serialVersionUID = -6283899245869146155L;

	/**
	 * Creates a new {@link ItemNotFoundException} with a item id.
	 * 
	 * @param id
	 *            - item id
	 */
	public ItemNotFoundException(String id) {
		super("Not found: " + id);
	}
}
