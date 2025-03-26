package rs.raf.chat_application_api.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import rs.raf.chat_application_api.configuration.exception.EntityNotFoundException;
import rs.raf.chat_application_api.configuration.exception.UserNotFoundException;
import rs.raf.chat_application_api.model.ChatMessage;
import rs.raf.chat_application_api.model.ChatMessageDTO;
import rs.raf.chat_application_api.model.User;
import rs.raf.chat_application_api.service.ChatMessageService;
import rs.raf.chat_application_api.service.UserService;

@RestController
@RequestMapping("/message")
public class ChatMessageController extends RestControllerImpl<ChatMessage, ChatMessageDTO, Long>{

	@Autowired
	private UserService userService;
	
	@Autowired
	public ChatMessageController(ChatMessageService messageService) {
		super(messageService);
	}
	
	@GetMapping(value = "/sent-message/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> findAllUserSentMessages(@PathVariable("userId") Long userId) {
		
		User user = this.userService.getById(userId);
		if(user == null) {
			throw new EntityNotFoundException("User does not exist with id: " + userId);				
		}
		
		List<ChatMessage> userSentMessages = ((ChatMessageService)super.service).findAllUserSentMessages(userId);
		return new ResponseEntity<List<ChatMessage>>(userSentMessages, HttpStatus.OK);
	}
	
	@GetMapping(value = "/received-message/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> findAllUserReceivedMessages(@PathVariable("userId") Long userId) {
		
		User user = this.userService.getById(userId);
		if(user == null) {
			throw new EntityNotFoundException("User does not exist with id: " + userId);
		}
		
		List<ChatMessage> userReceivedMessages = ((ChatMessageService)super.service).findAllUserReceivedMessages(userId);
		return new ResponseEntity<List<ChatMessage>>(userReceivedMessages, HttpStatus.OK);
	}
	
	@GetMapping(value = "/all-message/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> findAllUserMessages(@PathVariable("userId") Long userId) {
		
		User user = this.userService.getById(userId);
		if(user == null) {
			throw new EntityNotFoundException("User does not exist with id: " + userId);	
		}
		
		List<ChatMessage> userMessages = ((ChatMessageService)super.service).findAllUserMessages(userId);
		return new ResponseEntity<List<ChatMessage>>(userMessages, HttpStatus.OK);
	}
	
	/**
	 * Gets all Messages where userSenderId is sender and userReceiverId is receiver
	 * @param userSenderId
	 * @param userReceiverId
	 * @return messages
	 */
	@GetMapping(value = "/all-message/{userSenderId}/{userReceiverId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAllUserSenderAndUserReceiverMessages(@PathVariable("userSenderId") Long userSenderId, @PathVariable("userReceiverId") Long userReceiverId) {
		
		// Check if User ids exists
		boolean userSenderExists = this.userService.existById(userSenderId);
		boolean userReceiverExists = this.userService.existById(userReceiverId);
		
		try {
			if(userSenderExists == false) {
				throw new UserNotFoundException("userSender not exist with id: " + userSenderId);
			}
			if(userReceiverExists == false) {
				throw new UserNotFoundException("userReceiver not exist with id: " + userSenderId);
			}	
		} catch (UserNotFoundException e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		
		// If Users exist
		List<ChatMessage> messages = ((ChatMessageService)super.service).getAllUserSenderAndUserReceiverMessages(userSenderId, userReceiverId);
		List<ChatMessageDTO> messagesDto = new ArrayList<ChatMessageDTO>();
		for (ChatMessage message: messages) {
			ChatMessageDTO messageDto = new ChatMessageDTO(message.getId(), message.getUserSender().getId(), message.getUserReceiver().getId(), message.getMessageContent(), message.getTimeCreated());
			messagesDto.add(messageDto);
		}
		
		return new ResponseEntity<List<ChatMessageDTO>>(messagesDto, HttpStatus.OK);
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Override
	public ResponseEntity<?> save(@RequestBody @Valid ChatMessageDTO messageDto) {
		
		// Load UserSender and userReceiver
		User userSender = this.userService.getById(messageDto.getUserSenderId());
		User userReceiver = this.userService.getById(messageDto.getUserReceiverId());
		
		// If Message userSender or userReceiver not found 
		if(userSender == null || messageDto.getUserSenderId() == null) {
			throw new EntityNotFoundException("Message userSender not found with id: " + messageDto.getUserSenderId());	
		}
		if(userReceiver == null || messageDto.getUserReceiverId() == null) {	
			throw new EntityNotFoundException("Message userReceiver not found with id: " + messageDto.getUserReceiverId());
		}
		
		// Message save() Validation
		try {
			if(messageDto.getId() != null) {
				throw new Exception("MessageDTO attribute Id should not be defined for save() route.");
			}
			if(messageDto.getMessageContent() == null) {
				throw new Exception("MessageDTO attribute messageContent should not be undefined for save() route.");
			}
			if(messageDto.getTimeCreated() != null) {
				throw new Exception("MessageDTO attribute timeCreated should not be defined for save() route.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE); 
		}
		
		// Save Message
		ChatMessage message = messageDto.transform(userSender, userReceiver);
//		message.setTimeCreated(LocalDateTime.now());
		
		message = ((ChatMessageService)super.service).save(message);
		
		return new ResponseEntity<ChatMessage>(message, HttpStatus.OK);
	}
	
	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Override
	public ResponseEntity<?> update(@RequestBody @Valid ChatMessageDTO messageDto) {

		// Load UserSender and userReceiver
		User userSender = this.userService.getById(messageDto.getUserSenderId());
		User userReceiver = this.userService.getById(messageDto.getUserReceiverId());
		
		ChatMessage findMessage = ((ChatMessageService)super.service).getById(messageDto.getId());
		if(findMessage == null) {
			throw new EntityNotFoundException("Message not found with id: " + messageDto.getId());		
		}
		
		if(findMessage.getUserSender().getId() != messageDto.getUserSenderId()) {
			try {
				throw new Exception("Can not change message userSender id: " + messageDto.getUserSenderId());
			} catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
			}			
		}
		
		if(findMessage.getUserReceiver().getId() != messageDto.getUserReceiverId()) {
			try {
				throw new Exception("Can not change message userReceiver id: " + messageDto.getUserSenderId());
			} catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
			}			
		}
		
		// If Message userSender or userReceiver not found 
		if(userSender == null || messageDto.getUserSenderId() == null) {			
			throw new EntityNotFoundException("Message userSender not found with id: " + messageDto.getUserSenderId());
		}

		if(userReceiver == null || messageDto.getUserReceiverId() == null) {			
			throw new EntityNotFoundException("Message userReceiver not found with id: " + messageDto.getUserReceiverId());	
		}
		
		// Message update() validation
		try {
			if(messageDto.getId() == null) {
				throw new Exception("MessageDTO attribute id should be defined for update() route.");
			}
			if(messageDto.getMessageContent().isEmpty() || messageDto.getMessageContent() == null  ) {
				throw new Exception("MessageDTO attribute messageContent should be defined for update() route.");
			}
			if(messageDto.getTimeCreated() == null) {
				throw new Exception("MessageDTO attribute timeCreated should be defined for update() route.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE); 
		}
		
		// Update Message
		ChatMessage message = messageDto.transform(userSender, userReceiver);
		message = ((ChatMessageService)super.service).update(message, message.getId());
		
		return new ResponseEntity<ChatMessage>(message, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/{messageId}")
	@Override
	public ResponseEntity<?> delete(@PathVariable("messageId") Long messageId) {

		// Find Message
		ChatMessage message = ((ChatMessageService)super.service).getById(messageId);
		if(message == null) {	
			new EntityNotFoundException("Message not found with id: " + messageId);
		}
		
		((ChatMessageService)super.service).delete(message.getId());

		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	
}
