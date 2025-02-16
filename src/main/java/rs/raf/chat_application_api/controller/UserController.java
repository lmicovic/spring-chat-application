package rs.raf.chat_application_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.raf.chat_application_api.model.User;
import rs.raf.chat_application_api.service.UserService;

@RestController
@RequestMapping(value = "/user")
public class UserController extends RestControllerImpl<User, Long>{

	@Autowired
	public UserController(UserService userService) {
		super(userService);
	}
	
	@GetMapping(value = "/email/{email}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> existsByEmail(@PathVariable("email") String email) {		
		boolean exists = ((UserService)super.service).existsByEmail(email);
		return new ResponseEntity<Boolean>(exists, HttpStatus.OK);
	}
	
}
