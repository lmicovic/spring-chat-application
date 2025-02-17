package rs.raf.chat_application_api.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.raf.chat_application_api.model.Message;
import rs.raf.chat_application_api.model.User;
import rs.raf.chat_application_api.repository.MessageRepository;
import rs.raf.chat_application_api.repository.UserRepository;

@Service
public class MessageService extends RestServiceImpl<Message, Long> {

	private UserRepository userRepository;
	
	@Autowired
	public MessageService(MessageRepository messageRepository, UserRepository userRepository) {
		super(messageRepository);
		this.userRepository = userRepository;
	}
	
	/**
	 * Retrieves All Sent Messages for Specific User.
	 * 
	 * @param userId
	 * @return messages - all user sent messages or null if User with specified ID does not exists in database
	 */
	public List<Message> findAllUserSentMessages(Long userId) {
		
		// User does not Exist
		if(this.checkUserExists(userId) == false) {
			return null;
		}
		
		// Get All Sent Messages for specific User
		List<Message> usersMessages = ((MessageRepository)super.repository).findByUserSenderId(userId);
		return usersMessages;

	}
	
	/**
	 * Retrieves All Received Messages for Specific User.
	 * 
	 * @param userId
	 * @return messages - all user received messages or null if User with specified ID does not exists in database
	 */
	public List<Message> findAllUserReceivedMessages(Long userId) {
		
		// User does not Exist
		if(this.checkUserExists(userId) == false) {
			return null;
		}

		// Get All Received Messages for specific User
		List<Message> usersRecievedMessages = ((MessageRepository)super.repository).findByUserReceiverId(userId);
		return usersRecievedMessages;
	}
	
	/**
	 * Retrieves All User's Messages (userSentMessages and userReceivedMessages) for Specific User.
	 * 
	 * @param userId
	 * @return messages - all user received messages or null if User with specified ID does not exists in database
	 */
	public List<Message> findAllUserMessages(Long userId) {
		
		// User does not Exist
		if(this.checkUserExists(userId) == false) {
			return null;
		}
		
		// Gets All User's messages, userSentMessages and userReceivedMessages
		List<Message> userMessages = ((MessageRepository)super.repository).findAllUserMessages(userId);
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
