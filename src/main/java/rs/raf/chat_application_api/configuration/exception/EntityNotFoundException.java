package rs.raf.chat_application_api.configuration.exception;

/**
 * This exception is triggered if Model Entity is not found in Database.
 */
public class EntityNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 7784905681898779049L;;

	public EntityNotFoundException(String message) {
		super(message);
	}
	
	public EntityNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
