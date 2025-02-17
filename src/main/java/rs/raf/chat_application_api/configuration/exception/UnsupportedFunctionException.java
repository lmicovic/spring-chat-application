package rs.raf.chat_application_api.configuration.exception;

public class UnsupportedFunctionException extends RuntimeException {

	private static final long serialVersionUID = -4019533203946705529L;

	public UnsupportedFunctionException(String message) {
		super(message);
	}
	
	public UnsupportedFunctionException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
