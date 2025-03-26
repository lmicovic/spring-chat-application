package rs.raf.chat_application_api.service;

import java.util.List;  
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rs.raf.chat_application_api.configuration.exception.EntityNotFoundException;
import rs.raf.chat_application_api.configuration.exception.UserNotFoundException;
import rs.raf.chat_application_api.model.ChatMessage;
import rs.raf.chat_application_api.model.User;
import rs.raf.chat_application_api.repository.ChatMessageRepository;
import rs.raf.chat_application_api.repository.UserRepository;

@Service
public class UserService extends RestServiceImpl<User, Long> implements UserDetailsService {
	
	@Autowired
	private ChatMessageRepository messageRepository;
	
	private PasswordEncoder encoder = new BCryptPasswordEncoder();
	
	public UserService(JpaRepository<User, Long> repository) {
		super(repository);
	}
	
	@Override
	public User save(User user) throws DataIntegrityViolationException {
		user.setPassword(encoder.encode(user.getPassword()));
		return ((UserRepository)super.repository).save(user);
	}
	
	@Override
	public List<User> saveAll(List<User> users) {
		
		for (User user : users) {
			user.setPassword(encoder.encode(user.getPassword()));	
		}
		
		return super.saveAll(users);
	}
	
	@Override
	public User update(User user, Long id) {
		
//		user.setPassword(encoder.encode(user.getPassword()));
		return super.update(user, id);
	}
	
	@Override
	public void delete(Long userId) throws EntityNotFoundException {
		
		// Find User
		Optional<User> user = ((UserRepository)super.repository).findById(userId);
		if(user.isEmpty()) {
			throw new EntityNotFoundException("User not found with id: " + userId);
		}
		
		// Check if User has any Messages
		List<ChatMessage> usersMessages = this.messageRepository.findAllUserMessages(userId);
		
		// If user has any message, first delete all his messages
		if(usersMessages.isEmpty() == false || usersMessages == null) {
			for (ChatMessage usersMessage : usersMessages) {
				this.messageRepository.delete(usersMessage);
			}	
		}
		
		// Delete User
		((UserRepository)super.repository).delete(user.get());
		
	}
	
	/**
	 * Checks If user with specific Email exists.
	 * @param email
	 * @return boolean
	 */
	public boolean existsByEmail(String email) {
		return ((UserRepository)super.repository).existsByEmail(email);
	}
	
	/**
	 * Checks If user with specific id exists.
	 * @param userId
	 * @return boolean
	 */
	public boolean existById(Long userId) {
		
		Optional<User> user = ((UserRepository)super.repository).findById(userId);
		if(user.isEmpty() == true) {
			return false;
		}
		
		return true;
	}
	
	public User findUserByEmail(String email) {
		Optional<User> foundUser = ((UserRepository)super.repository).findByEmail(email);
		if(foundUser.isEmpty()) {
			return null;
		}
		
		return foundUser.get();
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		UserDetails userDetails = this.findUserByEmail(username);
		if(userDetails == null) {
			return null;
		}
		
		return userDetails;
	}
	
	/**
	 * Adds selected User to logged User friend list.
	 * @param loggedUserId - logged user that wants to add selected user to friend list
	 * @param addUserId - user that has to be added to friend list
	 * @return loggedUser
	 */
	public User addUserToFriendList(Long loggedUserId, Long addUserId) throws UserNotFoundException {
		
		Optional<User> loggedUser = ((UserRepository)super.repository).findById(loggedUserId);
		Optional<User> addUser = ((UserRepository)super.repository).findById(addUserId);
		
		// Check if Users Exists
		if(loggedUser.isEmpty() == true) {
			throw new UserNotFoundException("Logged User not Found. User not found with id: " + loggedUserId);
		}
		
		if(addUser.isEmpty() == true) {
			throw new UserNotFoundException("Selected User not Found. User not found with id: " + addUserId);
		}
		
		// Add User to Users Friend List
		loggedUser.get().addToFriendList(addUser.get());
		
		return ((UserRepository)super.repository).save(loggedUser.get());
	}
	
	
}
