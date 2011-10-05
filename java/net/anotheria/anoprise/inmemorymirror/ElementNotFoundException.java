package net.anotheria.anoprise.inmemorymirror;

public class ElementNotFoundException extends InMemoryMirrorException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ElementNotFoundException(String message){
		super(message);
	}
	public ElementNotFoundException(String message, Throwable cause){
		super(message, cause);
	}

}
