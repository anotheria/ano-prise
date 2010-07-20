package net.anotheria.anoprise.eventserviceV2;

/**
 * Exception, thrown by EventService.
 * 
 * @author vkazhdan
 */
public class EventServiceException extends Exception {
	
	private static final long serialVersionUID = -155482511589687L;

	public EventServiceException() {
		super();
	}

	public EventServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public EventServiceException(String message) {
		super(message);
	}

	public EventServiceException(Throwable cause) {
		super(cause);
	}
}
