package rs.raf.chat_application_api.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.raf.chat_application_api.configuration.exception.UnsupportedFunctionException;

/**
 * Data Transfer Object Class for Message.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO implements Serializable {
	 
	private static final long serialVersionUID = 468183503571635466L;

	private Long id;
	private UserDTO userSender;
	private UserDTO userReceiver;
	private String messageContent;
	private LocalDateTime timeCreated;
	
	public MessageDTO(UserDTO userSender, UserDTO userReceiver, String messageContent, LocalDateTime timeCreated) {
		this.userSender = userSender;
		this.userReceiver = userReceiver;
		this.messageContent = messageContent;
		this.timeCreated = timeCreated;
	}

	/**
	 * Transform MessageDTO to Message Entity
	 * @return message - message Entity
	 */
	public Message transform() throws UnsupportedFunctionException {		
		throw new UnsupportedFunctionException("rs.raf.chat_application_api.model.MessageDTO#transform() method is not supported yet.");
	}

	/**
	 * Transform MessageDTO to Message Entity
	 * @return message - message Entity
	 */
	public Message transform(User userSender, User userReceiver) {
		
		Message message = new Message();
		
		message.setId(this.id);
		message.setUserSender(userSender);
		message.setUserReceiver(userReceiver);
		message.setMessageContent(messageContent);
		message.setTimeCreated(timeCreated);
		
		return message;
	}
}
