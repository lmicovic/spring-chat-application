package rs.raf.chat_application_api.model;

import java.io.Serializable; 
import java.time.LocalDateTime;
import java.util.Date;
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
public class ChatMessageDTO implements Serializable {
	 
	private static final long serialVersionUID = 468183503571635466L;

	private Long id;
	private Long userSenderId;
	private Long userReceiverId;
	private String messageContent;
	private Date timeCreated;
	
	public ChatMessageDTO(Long userSenderId, Long userReceiverId, String messageContent) {	
		this.userSenderId = userSenderId;
		this.userReceiverId = userReceiverId;
		this.messageContent = messageContent;
	}
	
	public ChatMessageDTO(Long userSenderId, Long userReceiverId, String messageContent, Date timeCreated) {	
		this.userSenderId = userSenderId;
		this.userReceiverId = userReceiverId;
		this.messageContent = messageContent;
		this.timeCreated = timeCreated;
	}
	
	public ChatMessage transform(User userSender, User userReceiver) {
		
		ChatMessage message = new ChatMessage();
		message.setUserSender(userSender);
		message.setUserReceiver(userReceiver);
		message.setMessageContent(this.messageContent);
		message.setTimeCreated(this.timeCreated);
		
		return message;
		
	}
	
}
