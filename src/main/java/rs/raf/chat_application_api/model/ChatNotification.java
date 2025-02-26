package rs.raf.chat_application_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

/**
 * When the server receives a chat message, it doesn’t send it directly to the client, rather, it sends a chat notification, to notify the client there is a new message received, then the client can pull the new message. The message will be marked as delivered once the client pulls the message
 */
@Entity
@Data
@NoArgsConstructor
public class ChatNotification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "sender_id", nullable =  false)
	private User userSender;
	
	@ManyToOne
	@JoinColumn(name = "receiver_id", nullable =  false)
	private User userReceiver;
	
	public ChatNotification(User userSender, User userReceiver) {
		this.userSender = userSender;
		this.userReceiver = userReceiver;
	}
	
}
