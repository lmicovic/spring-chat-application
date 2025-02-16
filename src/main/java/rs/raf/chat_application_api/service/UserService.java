package rs.raf.chat_application_api.service;

import org.springframework.stereotype.Service;

import rs.raf.chat_application_api.model.User;
import rs.raf.chat_application_api.repository.UserRepository;

@Service
public class UserService extends RestServiceImpl<User, Long> {

	public UserService(UserRepository userRepository) {
		super(userRepository);
	}	
	
	/**
	 * Checks If user with specific Email exists.
	 * @param email
	 * @return boolean
	 */
	public boolean existsByEmail(String email) {
		return ((UserRepository)super.repository).existsByEmail(email);
	}
	
}
