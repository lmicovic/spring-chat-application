package rs.raf.chat_application_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.raf.chat_application_api.model.User;
import rs.raf.chat_application_api.service.UserService;

@RestController
@RequestMapping(value = "/user")
@Validated
public class UserController extends RestControllerImpl<User, Long>{

	@Autowired
	public UserController(UserService userService) {
		super(userService);
	}
	
}
