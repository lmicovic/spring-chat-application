package rs.raf.chat_application_api.configuration.exception;

public class UserNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 3844355356312289200L;

	public UserNotFoundException(String message) {
		super(message);
	}
	
	public UserNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
