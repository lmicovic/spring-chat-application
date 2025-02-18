package rs.raf.chat_application_api.controller;

import java.time.LocalDateTime;
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
import rs.raf.chat_application_api.model.Message;
import rs.raf.chat_application_api.model.MessageDTO;
import rs.raf.chat_application_api.model.User;
import rs.raf.chat_application_api.service.MessageService;
import rs.raf.chat_application_api.service.UserService;

@RestController
@RequestMapping("/message")
public class MessageController extends RestControllerImpl<Message,MessageDTO, Long>{

	@Autowired
	private UserService userService;
	
	@Autowired
	public MessageController(MessageService messageService) {
		super(messageService);
	}
	
	@GetMapping(value = "/sent-message/{userId}")
	public ResponseEntity<?> findAllUserSentMessages(@PathVariable("userId") Long userId) {
		
		List<Message> userSentMessages = ((MessageService)super.service).findAllUserSentMessages(userId);
		if(userSentMessages == null) {
			try {
				throw new EntityNotFoundException("User does not exist with Id: " + userId);
			} catch (EntityNotFoundException e) {
				e.printStackTrace();
				return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
			}			
		}
		
		return new ResponseEntity<List<Message>>(userSentMessages, HttpStatus.OK);
	}
	
	@GetMapping(value = "/received-message/{userId}")
	public ResponseEntity<?> findAllUserReceivedMessages(@PathVariable("userId") Long userId) {
		
		List<Message> userReceivedMessages = ((MessageService)super.service).findAllUserReceivedMessages(userId);
		if(userReceivedMessages == null) {
			try {
				throw new EntityNotFoundException("User does not exist with Id: " + userId);
			} catch (EntityNotFoundException e) {
				e.printStackTrace();
				return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
			}
			
		}
		
		return new ResponseEntity<List<Message>>(userReceivedMessages, HttpStatus.OK);
	}
	
	@GetMapping(value = "/all-message/{userId}")
	public ResponseEntity<?> findAllUserMessages(@PathVariable("userId") Long userId) {
		
		List<Message> userMessages = ((MessageService)super.service).findAllUserMessages(userId);
		if(userMessages == null) {
			try {
				throw new EntityNotFoundException("User does not exist with Id: " + userId);
			} catch (EntityNotFoundException e) {
				e.printStackTrace();
				return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
			}			
		}
		
		return new ResponseEntity<List<Message>>(userMessages, HttpStatus.OK);
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Override
	public ResponseEntity<?> save(@RequestBody @Valid MessageDTO messageDto) {
		
		// Load UserSender and userReceiver
		User userSender = this.userService.getById(messageDto.getUserSender().getId());
		User userReceiver = this.userService.getById(messageDto.getUserReceiver().getId());
		
		// If Message userSender or userReceiver not found 
		if(userSender == null || messageDto.getUserSender().getId() == null) {
			try {
				throw new EntityNotFoundException("Message userSender not found with id: " + messageDto.getUserSender().getId());
			} catch (EntityNotFoundException e) {
				e.printStackTrace();
				return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
			}
		}
		if(userReceiver == null || messageDto.getUserReceiver().getId() == null) {
			try {
				throw new EntityNotFoundException("Message userReceiver not found with id: " + messageDto.getUserReceiver().getId());
			} catch (EntityNotFoundException e) {
				e.printStackTrace();
				return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
			}			
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
		Message message = messageDto.transform(userSender, userReceiver);
		message.setTimeCreated(LocalDateTime.now());
		
		message = ((MessageService)super.service).save(message);
		
		return new ResponseEntity<Message>(message, HttpStatus.OK);
	}
	
	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Override
	public ResponseEntity<?> update(@RequestBody @Valid MessageDTO messageDto) {

		// Load UserSender and userReceiver
		User userSender = this.userService.getById(messageDto.getUserSender().getId());
		User userReceiver = this.userService.getById(messageDto.getUserReceiver().getId());
		
		Message findMessage = ((MessageService)super.service).getById(messageDto.getId());
		if(findMessage == null) {
			try {
				throw new EntityNotFoundException("Message not found with id: " + messageDto.getId());
			} catch (EntityNotFoundException e) {
				e.printStackTrace();
				return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
			}			
		}
		
		if(findMessage.getUserSender().getId() != messageDto.getUserSender().getId()) {
			try {
				throw new Exception("Can not change message userSender id: " + messageDto.getUserSender().getId());
			} catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
			}			
		}
		
		if(findMessage.getUserReceiver().getId() != messageDto.getUserReceiver().getId()) {
			try {
				throw new Exception("Can not change message userReceiver id: " + messageDto.getUserSender().getId());
			} catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
			}			
		}
		
		// If Message userSender or userReceiver not found 
		if(userSender == null || messageDto.getUserSender().getId() == null) {
			try {
				throw new EntityNotFoundException("Message userSender not found with id: " + messageDto.getUserSender().getId());
			} catch (EntityNotFoundException e) {
				e.printStackTrace();
				return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
			}
		}

		if(userReceiver == null || messageDto.getUserReceiver().getId() == null) {			
			try {
				throw new EntityNotFoundException("Message userReceiver not found with id: " + messageDto.getUserReceiver().getId());
			} catch (EntityNotFoundException e) {
				e.printStackTrace();
				return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
			}
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
		Message message = messageDto.transform(userSender, userReceiver);
		message = ((MessageService)super.service).update(message, message.getId());
		
		return new ResponseEntity<Message>(message, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/{messageId}")
	@Override
	public ResponseEntity<?> delete(@PathVariable("messageId") Long messageId) {

		// Find Message
		Message message = ((MessageService)super.service).getById(messageId);
		if(message == null) {
			try {
				new EntityNotFoundException("Message not found with id: " + messageId);
			} catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
			}
		}
		
		((MessageService)super.service).delete(message.getId());

		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	
}
