package net.anotheria.anoprise.inmemorymirror;

public class InMemoryMirrorException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public InMemoryMirrorException(String message){
		super(message);
	}
	public InMemoryMirrorException(String message, Throwable cause){
		super(message, cause);
	}
}
 