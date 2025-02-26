package rs.raf.chat_application_api.model;

import java.io.Serializable;
import org.springframework.http.HttpStatus;
import lombok.Data;

@Data
public class ErrorMessageDTO implements Serializable {

	private static final long serialVersionUID = 4775984961674944143L;

	private Exception exception;
	private HttpStatus messageStatus;
	
	public ErrorMessageDTO() {

	}
	
	public ErrorMessageDTO(Exception exception) {
		this.exception = exception;
	}
	
	public ErrorMessageDTO(Exception exception, HttpStatus messageStatus) {
		this.exception = exception;
		this.messageStatus = messageStatus;
	}
		
}
