package net.anotheria.anoprise.dualcrud;

public class CrudServiceException extends Exception {
	public CrudServiceException(String message){
		super(message);
	}
	
	public CrudServiceException(String message, Throwable cause){
		super(message, cause);
	}
}
