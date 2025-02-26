package rs.raf.chat_application_api.service;

import java.util.List;  
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.raf.chat_application_api.model.ChatMessage;
import rs.raf.chat_application_api.model.User;
import rs.raf.chat_application_api.repository.ChatMessageRepository;
import rs.raf.chat_application_api.repository.UserRepository;

@Service
public class ChatMessageService extends RestServiceImpl<ChatMessage, Long> {

	private UserRepository userRepository;
	
	@Autowired
	public ChatMessageService(ChatMessageRepository messageRepository, UserRepository userRepository) {
		super(messageRepository);
		this.userRepository = userRepository;
	}
	
	/**
	 * Retrieves All Sent Messages for Specific User.
	 * 
	 * @param userId
	 * @return messages - all user sent messages or null if User with specified ID does not exists in database
	 */
	public List<ChatMessage> findAllUserSentMessages(Long userId) {
		
		// User does not Exist
		if(this.checkUserExists(userId) == false) {
			return null;
		}
		
		// Get All Sent Messages for specific User
		List<ChatMessage> usersMessages = ((ChatMessageRepository)super.repository).findByUserSenderId(userId);
		return usersMessages;

	}
	
	/**
	 * Retrieves All Received Messages for Specific User.
	 * 
	 * @param userId
	 * @return messages - all user received messages or null if User with specified ID does not exists in database
	 */
	public List<ChatMessage> findAllUserReceivedMessages(Long userId) {
		
		// User does not Exist
		if(this.checkUserExists(userId) == false) {
			return null;
		}

		// Get All Received Messages for specific User
		List<ChatMessage> usersRecievedMessages = ((ChatMessageRepository)super.repository).findByUserReceiverId(userId);
		return usersRecievedMessages;
	}
	
	/**
	 * Retrieves All User's Messages (userSentMessages and userReceivedMessages) for Specific User.
	 * 
	 * @param userId
	 * @return messages - all user received messages or null if User with specified ID does not exists in database
	 */
	public List<ChatMessage> findAllUserMessages(Long userId) {
		
		// User does not Exist
		if(this.checkUserExists(userId) == false) {
			return null;
		}
		
		// Gets All User's messages, userSentMessages and userReceivedMessages
		List<ChatMessage> userMessages = ((ChatMessageRepository)super.repository).findAllUserMessages(userId);
		return userMessages;
	}
	
	/**
	 * Checks if User with specific Id is present in Database.
	 * @param userId
	 * @return true or false
	 */
	private boolean checkUserExists(Long userId) {
		
		Optional<User> findUser = this.userRepository.findById(userId);
		if(findUser.isEmpty()) {
			return false;
		}
		
		return true;
	}
	
}
