package rs.raf.chat_application_api.configuration;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import rs.raf.chat_application_api.configuration.enums.UserRole;
import rs.raf.chat_application_api.model.User;
import rs.raf.chat_application_api.service.UserService;

@Component
public class Runner implements ApplicationRunner {

	private UserService userService;
	
	@Autowired
	public Runner(UserService userService) {
		this.userService = userService;
	}
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		this.initializeDatabase();
	}
	
	private void initializeDatabase() {
		this.initializeUsers();
	}
	
	private void initializeUsers() {
	
		List<UserRole> userRoles = new ArrayList<UserRole>();
		userRoles.add(UserRole.ROLE_USER);
		userRoles.add(UserRole.ROLE_ADMIN);
		
		 List<User> users = new ArrayList<User>() {
			 {
				 add(new User("Pera", "Peric", "peraperic1@gmail.com", "Test!123!", UserRole.ROLE_USER));
				 add(new User("Ana", "Peric", "peraperic2@gmail.com", "Test!123!", UserRole.ROLE_ADMIN));
				 add(new User("Mika", "Anic", "peraperic21@gmail.com", "Test!123!", userRoles));
			 }
		 };
		 
		 users = this.userService.saveAll(users);
		 
	}
	
}
