package rs.raf.chat_application_api.repository;

import java.util.List; 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.raf.chat_application_api.model.ChatMessage;

/**
 * Repository for retrieving messages from Database.
 */
@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

	/**
	 * Retrieves All Sent Messages for Specific User from Data Base.
	 * @param userSenderId
	 * @return messages - user sent messages
	 */
	List<ChatMessage> findByUserSenderId(Long userSenderId);
	
	/**
	 * Retrieves All Received Messages for Specific User from Data Base.
	 * @param userSenderId
	 * @return messages - user sent messages
	 */
	List<ChatMessage> findByUserReceiverId(Long userSenderId);
	
	/**
	 * Retrieves All Messages (userSentMessages and userReceivedMessages) for Specific User from Data Base.
	 * @param userSenderId
	 * @return messages - all user's (userSentMessages and userReceivedMessages) messages
	 */
	@Query("SELECT m FROM messages m WHERE m.userSender.id = :userId OR m.userReceiver.id = :userId")
	List<ChatMessage> findAllUserMessages(Long userId);
	
	/**
	 * Retrieves All Messages where sender is userSenderId and receiver is userReceiverId
	 * @param userSenderId
	 * @param userReceiverId
	 * @return messages
	 */
	@Query("SELECT m FROM messages m WHERE " +
	           "(m.userSender.id = :userSenderId AND m.userReceiver.id = :userReceiverId) OR " +
	           "(m.userSender.id = :userReceiverId AND m.userReceiver.id = :userSenderId) " + 
	           "ORDER BY m.timeCreated ASC")
	    List<ChatMessage> getAllUserSenderAndUserReceiverMessages(@Param("userSenderId") Long userSenderId, @Param("userReceiverId") Long userReceiverId);
	
}
