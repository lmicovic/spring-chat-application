package rs.raf.chat_application_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import rs.raf.chat_application_api.model.Message;

/**
 * Repository for retrieving messages from Database.
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

	/**
	 * Retrieves All Sent Messages for Specific User from Data Base.
	 * @param userSenderId
	 * @return messages - user sent messages
	 */
	List<Message> findByUserSenderId(Long userSenderId);
	
	/**
	 * Retrieves All Received Messages for Specific User from Data Base.
	 * @param userSenderId
	 * @return messages - user sent messages
	 */
	List<Message> findByUserReceiverId(Long userSenderId);
	
	/**
	 * Retrieves All Messages (userSentMessages and userReceivedMessages) for Specific User from Data Base.
	 * @param userSenderId
	 * @return messages - all user's (userSentMessages and userReceivedMessages) messages
	 */
	@Query("SELECT m FROM messages m WHERE m.userSender.id = :userId OR m.userReceiver.id = :userId")
	List<Message> findAllUserMessages(Long userId);
	
}
