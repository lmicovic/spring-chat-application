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

import rs.raf.chat_application_api.model.User;
import rs.raf.chat_application_api.repository.UserRepository;

@Service
public class UserService extends RestServiceImpl<User, Long> implements UserDetailsService {

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
