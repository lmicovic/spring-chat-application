package rs.raf.chat_application_api.model;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity Class for User's messages.
 */
@Entity(name = "messages")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "sender_id", nullable = false)
	@JsonManagedReference
	private User userSender;
	
	@ManyToOne
	@JoinColumn(name = "receiver_id", nullable =  false)
	@JsonManagedReference
	private User userReceiver;
	
	@Column(name = "content", nullable = false, length = 250)
	private String messageContent;
	
	@Column(name = "time_created")
	private LocalDateTime timeCreated;
	
	public Message(User userSender, User userReceiver, String messageContent) {
		this.userSender = userSender;
		this.userReceiver = userReceiver;
		this.messageContent = messageContent;
		this.timeCreated = LocalDateTime.now();
	}
	
	
	
}
