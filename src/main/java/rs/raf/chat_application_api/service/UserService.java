package rs.raf.chat_application_api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import rs.raf.chat_application_api.configuration.exception.EntityNotFoundException;
import rs.raf.chat_application_api.model.Message;
import rs.raf.chat_application_api.model.User;
import rs.raf.chat_application_api.repository.MessageRepository;
import rs.raf.chat_application_api.repository.UserRepository;

@Service
public class UserService extends RestServiceImpl<User, Long> implements UserDetailsService {
	
	@Autowired
	private MessageRepository messageRepository;
	
	private PasswordEncoder encoder = new BCryptPasswordEncoder();

	@Autowired
	public UserService(UserRepository userRepository) {
		super(userRepository);
	}	
	
	@Override
	public User save(User user) throws DataIntegrityViolationException {
		user.setPassword(encoder.encode(user.getPassword()));
//		System.err.println(user.getPassword());
		return ((UserRepository)super.repository).save(user);
	}
	
	@Override
	public List<User> saveAll(List<User> users) {
		
		for (User user : users) {
			user.setPassword(encoder.encode(user.getPassword()));
//			System.err.println(user.getEncriptedPassword());
		}
		
		return super.saveAll(users);
	}
	
	@Override
	public User update(User user, Long id) {
		
		user.setPassword(encoder.encode(user.getPassword()));
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
		List<Message> usersMessages = this.messageRepository.findAllUserMessages(userId);
		
		// If user has any message, first delete all his messages
		if(usersMessages.isEmpty() == false || usersMessages == null) {
			for (Message usersMessage : usersMessages) {
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
	
}
