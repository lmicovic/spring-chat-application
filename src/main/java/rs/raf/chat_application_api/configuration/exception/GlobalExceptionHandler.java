package rs.raf.chat_application_api.configuration.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<Map<String, String>> handleConstraintsViolationException(ConstraintViolationException ex) {
		
		Map<String, String> errors = new HashMap<String, String>();
		
		Set<ConstraintViolation<?>> violations = ex.getConstraintViolations(); 
		for (ConstraintViolation<?> violation : violations) {
			errors.put(violation.getPropertyPath().toString(), violation.getMessage());
		}
		
		return new ResponseEntity<Map<String, String>>(errors, HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Returns HTTP response If one of Model Entity constraints is triggered.
	 * @param ex - exception that happened.
	 * @return map - field of Model Entity that was tringgered on constraints and contraint messages.
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
		
		Map<String, String> errors = new HashMap<>();
        
        // Get all field errors
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
		
		return new ResponseEntity<Map<String, String>>(errors, HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Returns HTTP Conflict Response if Unique Entity Value Constraint is triggered durning saving Entity in Data Base.
	 * This method extracts field name that violated Unique Value constraint in Table in Data Base.
	 * 
	 * @param ex - DataIntegrityViolationException Exception
	 * @return ResponseEntity that contains the name of field that caused Unique Value Violation in Table in Database, and triggered DataIntegrityViolationException Exception. 
	 */
	@ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleJdbcSQLIntegrityConstraintViolationException(DataIntegrityViolationException ex) {
		String fieldName = extractFieldName(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Field causing violation: " + fieldName);
    }

	/**
	 * Extracts Field Name in Data Base that caused DataIntegrityViolationException Exception.
	 * 
	 * @param message - message of DataIntegrityViolationException Exception.
	 * @return - field name that caused DataIntegrityViolationException Exception. 
	 */
    private String extractFieldName(String message) {
        
    	int firstIdx = message.indexOf("(");
    	int lastIdx = message.indexOf(")");
    	
    	String subString = message.substring(firstIdx, lastIdx+1);
    	String field = subString.replace("(", "").replace(")", "").split(" ")[0].toLowerCase();
    	
    	return field;
    }
	
}
