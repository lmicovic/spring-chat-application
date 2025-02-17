package rs.raf.chat_application_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rs.raf.chat_application_api.model.Message;
import rs.raf.chat_application_api.model.MessageDTO;
import rs.raf.chat_application_api.service.MessageService;

@RestController
@RequestMapping("/message")
public class MessageController extends RestControllerImpl<Message,MessageDTO, Long>{

	@Autowired
	public MessageController(MessageService messageService) {
		super(messageService);
	}
	
	@GetMapping(value = "/sent-message/{userId}")
	public ResponseEntity<?> findAllUserSentMessages(@PathVariable("userId") Long userId) {
		
		List<Message> userSentMessages = ((MessageService)super.service).findAllUserSentMessages(userId);
		if(userSentMessages == null) {
			return new ResponseEntity<String>("User does not exist with Id: " + userId, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<List<Message>>(userSentMessages, HttpStatus.OK);
	}
	
	@GetMapping(value = "/received-message/{userId}")
	public ResponseEntity<?> findAllUserReceivedMessages(@PathVariable("userId") Long userId) {
		
		List<Message> userReceivedMessages = ((MessageService)super.service).findAllUserReceivedMessages(userId);
		if(userReceivedMessages == null) {
			return new ResponseEntity<String>("User does not exist with Id: " + userId, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<List<Message>>(userReceivedMessages, HttpStatus.OK);
	}
	
	@GetMapping(value = "/all-message/{userId}")
	public ResponseEntity<?> findAllUserMessages(@PathVariable("userId") Long userId) {
		
		List<Message> userMessages = ((MessageService)super.service).findAllUserMessages(userId);
		if(userMessages == null) {
			return new ResponseEntity<String>("User does not exist with Id: " + userId, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<List<Message>>(userMessages, HttpStatus.OK);
	}
	
}
