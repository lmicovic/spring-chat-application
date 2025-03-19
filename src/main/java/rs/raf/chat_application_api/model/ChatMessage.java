package rs.raf.chat_application_api.model;
 
import java.util.Date;
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
 * Entity Class for Message.
 */
@Entity(name = "messages")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {

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
	
//	@ManyToOne
//	@JoinColumn(name = "chatRoomId")
//	@JsonManagedReference
//	private ChatRoom chatRoom;
	
//	@Column(name = "messageStatus")
//	private MessageStatus messageStatus;
	
	@Column(name = "content", nullable = false, length = 250)
	private String messageContent;
	
	@Column(name = "time_created")
	private Date timeCreated;
	
	public ChatMessage(User userSender, User userReceiver, String messageContent) {
		this.userSender = userSender;
		this.userReceiver = userReceiver;
		this.messageContent = messageContent;
		this.timeCreated = new Date();
//		this.messageStatus = MessageStatus.RECEIVED;
//		this.chatRoom = new ChatRoom(userSender, userReceiver);
	}
	
//	public ChatMessage(User userSender, User userReceiver, String messageContent, ChatRoom chatRoom) {
//		this.userSender = userSender;
//		this.userReceiver = userReceiver;
//		this.messageContent = messageContent;
//		this.timeCreated = new Date();
////		this.messageStatus = MessageStatus.RECEIVED;
////		this.chatRoom = chatRoom;
//	}
	
//	public ChatMessage(User userSender, User userReceiver, String messageContent, MessageStatus messageStatus, ChatRoom chatRoom) {
//		this.userSender = userSender;
//		this.userReceiver = userReceiver;
//		this.messageContent = messageContent;
//		this.timeCreated = new Date();
////		this.messageStatus = messageStatus;
////		this.chatRoom = chatRoom;
//	}
	
	
	
}
