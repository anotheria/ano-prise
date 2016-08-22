package net.anotheria.anoprise.inmemorymirror;

/**
 * <p>ElementNotFoundException class.</p>
 *
 * @author another
 * @version $Id: $Id
 */
public class ElementNotFoundException extends InMemoryMirrorException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * <p>Constructor for ElementNotFoundException.</p>
	 *
	 * @param message a {@link java.lang.String} object.
	 */
	public ElementNotFoundException(String message){
		super(message);
	}
	/**
	 * <p>Constructor for ElementNotFoundException.</p>
	 *
	 * @param message a {@link java.lang.String} object.
	 * @param cause a {@link java.lang.Throwable} object.
	 */
	public ElementNotFoundException(String message, Throwable cause){
		super(message, cause);
	}

}
