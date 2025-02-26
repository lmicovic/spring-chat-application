package rs.raf.chat_application_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class ChatRoom {

	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private Long id;
	
	/**
	 * chatId is generated by concatenating senderId_recipientId
	 */
	@Column(name = "chat_id")
	private String chatId;
	
	@ManyToOne
	@JoinColumn(name = "user_sender_id")
	private User userSender;
	
	@ManyToOne
	@JoinColumn(name = "user_receiver_id")
	private User userReceiver;
	
	public ChatRoom(User userSender, User userReceiver) {
		this.userSender = userSender;
		this.userReceiver = userReceiver;
		this.chatId = this.userSender.getId() + "_" + this.userReceiver.getId();
	}
	
}
