package rs.raf.chat_application_api.configuration.exception;

public class EntityExistException extends RuntimeException{

	private static final long serialVersionUID = -7957715196808056229L;

	public EntityExistException(String message) {
		super(message);
	}
	
	public EntityExistException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
