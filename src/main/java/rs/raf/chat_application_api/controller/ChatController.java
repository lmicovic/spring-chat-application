package rs.raf.chat_application_api.controller;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;
import rs.raf.chat_application_api.configuration.exception.UserNotFoundException;
import rs.raf.chat_application_api.model.ChatMessage;
import rs.raf.chat_application_api.model.ChatMessageDTO;
import rs.raf.chat_application_api.model.ErrorMessageDTO;
import rs.raf.chat_application_api.model.User;
import rs.raf.chat_application_api.service.ChatMessageService;
import rs.raf.chat_application_api.service.UserService;

@RestController
public class ChatController {

	private SimpMessagingTemplate messagingTemplate;
	private ChatMessageService chatMessageService;
//	private ChatRoomService chatRoomService;
//	private ChatNotificationService chatNotificationService; 
	private UserService userService;
	
	@Autowired
	public ChatController(SimpMessagingTemplate messagingTemplate, ChatMessageService chatMessageService, UserService userService) {
		this.messagingTemplate = messagingTemplate;
		this.chatMessageService = chatMessageService;
//		this.chatRoomService = chatRoomService;
//		this.chatNotificationService = chatNotificationService;
		this.userService = userService;
	}

	@MessageMapping("/chat")
	public void processMessage(ChatMessageDTO chatMessage) {
		
		// Print Received ChatMessage
		System.out.println(chatMessage);
		
		User userSender = this.userService.getById(chatMessage.getUserSenderId());
		User userReceiver = this.userService.getById(chatMessage.getUserReceiverId());
		
		//-------------------------------------------------------------------------------------------
		// [USER VALIDATION] - Check If userSender and userReceiver exists
		//-------------------------------------------------------------------------------------------
		try {
			if(userSender == null) {
				throw new UserNotFoundException("User sender not found with id: " + userSender + ", message is not sent.");
			}
			if(userReceiver == null) {
				throw new UserNotFoundException("User receiver not found with id: " + userReceiver + ", message is not sent.");
			}
		} catch (UserNotFoundException e) {
			
			e.printStackTrace();
			
			MessageHeaderAccessor headerAccessor = new MessageHeaderAccessor();
			headerAccessor.setHeader("message-type", "error");
			
			ErrorMessageDTO errorMessage = new ErrorMessageDTO(e, HttpStatus.NOT_FOUND);
			
			messagingTemplate.convertAndSend("/user/client" + chatMessage.getUserSenderId() + "/queue/messages", errorMessage, headerAccessor.getMessageHeaders());
			return;
		}
		//-------------------------------------------------------------------------------------------
		
		//-------------------------------------------------------------------------------------------
		// [SAVE MESSAGE]
		//-------------------------------------------------------------------------------------------
		this.saveMessage(chatMessage, userSender, userReceiver);
		//-------------------------------------------------------------------------------------------
		
		// Reroute ChatMessage to User Receiver
		messagingTemplate.convertAndSend("/user/client2/queue/messages", chatMessage);
		
	}
	
	/**
	 * Saves Message to Database
	 * @param messageDto
	 */
	private void saveMessage(ChatMessageDTO messageDto, User userSender, User userReceiver) {
		
		ChatMessage message = messageDto.transform(userSender, userReceiver);
		message = this.chatMessageService.save(message);
		
		System.out.println(message);
		
	}
	
}
