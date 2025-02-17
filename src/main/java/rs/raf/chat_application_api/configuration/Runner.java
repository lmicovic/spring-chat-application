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
import rs.raf.chat_application_api.model.Message;
import rs.raf.chat_application_api.model.User;
import rs.raf.chat_application_api.service.MessageService;
import rs.raf.chat_application_api.service.UserService;

@Component
public class Runner implements ApplicationRunner {

	private UserService userService;
	private MessageService messageService;
	
	@Autowired
	public Runner(UserService userService, MessageService messageService) {
		this.userService = userService;
		this.messageService = messageService;
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
		
		User user1 = new User("Pera", "Peric", "peraperic1@gmail.com", "Test!123!", UserRole.ROLE_USER);
		User user2 = new User("Ana", "Peric", "peraperic2@gmail.com", "Test!123!", UserRole.ROLE_ADMIN);
		User user3 = new User("Mika", "Anic", "peraperic21@gmail.com", "Test!123!", userRoles);
		
		// User1 send Message to User2
		Message message1 = new Message(user1, user2, "Test hello");
		
		List<User> users = new ArrayList<User>();
		users.add(user1);
		users.add(user2);
		users.add(user3);
		 
		users = this.userService.saveAll(users);
		this.messageService.save(message1);
		 
	}
	
}
