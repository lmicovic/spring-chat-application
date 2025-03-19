package rs.raf.chat_application_api.configuration;

import java.util.ArrayList;  
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import rs.raf.chat_application_api.configuration.enums.UserRole;
import rs.raf.chat_application_api.model.ChatMessage;
import rs.raf.chat_application_api.model.User;
import rs.raf.chat_application_api.service.ChatMessageService;
import rs.raf.chat_application_api.service.UserService;

@Component
public class Runner implements ApplicationRunner {

	private UserService userService;
	private ChatMessageService messageService;
//	private ChatRoomService chatRoomService;
//	private ChatNotificationService chatNotificationService;
	
	@Autowired
	public Runner(UserService userService, ChatMessageService messageService) {
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
		
		
		List<User> users = new ArrayList<User>();
		users.add(user1);
		users.add(user2);
		users.add(user3);
		
		// User1 send Message to User2
		users = this.userService.saveAll(users);
		user1 = users.get(0);
		user2 = users.get(1);
		user3 = users.get(2);
		
		this.userService.saveAll(users);
		
		ChatMessage message1 = new ChatMessage(user1, user2, "Hello!");
		this.messageService.save(message1);
		
	}
	
}
