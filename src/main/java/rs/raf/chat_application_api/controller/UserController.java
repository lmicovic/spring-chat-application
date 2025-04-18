	package rs.raf.chat_application_api.controller;

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
import rs.raf.chat_application_api.configuration.enums.UserRole;
import rs.raf.chat_application_api.configuration.exception.EntityExistException;
import rs.raf.chat_application_api.configuration.exception.EntityNotFoundException;
import rs.raf.chat_application_api.configuration.exception.UnsupportedFunctionException;
import rs.raf.chat_application_api.configuration.exception.UserNotFoundException;
import rs.raf.chat_application_api.model.User;
import rs.raf.chat_application_api.model.UserDTO;
import rs.raf.chat_application_api.service.UserService;

@RestController
@RequestMapping(value = "/user")
public class UserController extends RestControllerImpl<User, UserDTO, Long>{

	@Autowired
	public UserController(UserService userService) {
		super(userService);
	}
	
	@GetMapping(value = "/check-email/{email}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> existsByEmail(@PathVariable("email") String email) {		
		boolean exists = ((UserService)super.service).existsByEmail(email);
		return new ResponseEntity<Boolean>(exists, HttpStatus.OK);
	}
	
	@GetMapping(value = "/email/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
		
		User user = ((UserService)this.service).findUserByEmail(email);
		
		System.out.println(user);
		
		if(user == null) {
			try {
				throw new UserNotFoundException("User not found with email: " + email);
			} catch (UserNotFoundException e) {
				return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
			}
			
		}
		
		UserDTO userDto = new UserDTO(user.getId(), user.getFirstname(), user.getLastname(), user.getEmail(), user.getPassword());
				
		return new ResponseEntity<UserDTO>(userDto, HttpStatus.OK);
	}
	
	@GetMapping(value = "/current-user/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCurrentLoginUser(@PathVariable String email) {
		
		User user = ((UserService)this.service).findUserByEmail(email);
		
		System.out.println(user);
		
		if(user == null) {
			try {
				throw new UserNotFoundException("User not found with email: " + email);
			} catch (UserNotFoundException e) {
				return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
			}
			
		}
		
		user.setIsOnline(true);
		((UserService)super.service).update(user, user.getId());
		
		UserDTO userDto = new UserDTO(user.getId(), user.getFirstname(), user.getLastname(), user.getEmail(), user.getPassword(), user.getFriendList(), user.getAuthorities(), user.getIsOnline(), user.getLastOnline());
		
		return new ResponseEntity<UserDTO>(userDto, HttpStatus.OK);
	}
	
	@PostMapping(value = "/user-save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> saveUser(@RequestBody @Valid UserDTO userDto) {
		
		User user = userDto.transform(UserRole.ROLE_USER);
		
		// Check If User already exists
		boolean userExists = ((UserService)super.service).existsByEmail(user.getEmail());
		if(userExists == true) {
			try {
				throw new EntityExistException("User already existst with email: " + user.getEmail());
			} catch (EntityExistException e) {
				e.printStackTrace();
				return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
			}
		}
		
		// Save User
		user = ((UserService)super.service).save(user);
		
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	@PostMapping(value = "/user-save-all", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> saveAllUser(@RequestBody @Valid List<UserDTO> usersDto) {
		
		// Check If any User already exist
		List<User> users = new ArrayList<User>();
		for (UserDTO userDTO : usersDto) {
			try {
				User user = userDTO.transform(UserRole.ROLE_USER);
				boolean userExists = ((UserService)super.service).existsByEmail(user.getEmail());
				if(userExists == true) {
					throw new EntityExistException("User already existst with email: " + user.getEmail());
				}
				users.add(user);
			} catch (EntityExistException e) {
				e.printStackTrace();
				return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
			}
			
		}
		
		// Save all Users
		users = ((UserService)super.service).saveAll(users);
		
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}
	
	@PostMapping(value = "/admin-save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> saveAdmin(@RequestBody @Valid UserDTO userDto) {
		
		User user = userDto.transform(UserRole.ROLE_ADMIN);
		
		// Check If User already exists
		boolean userExists = ((UserService)super.service).existsByEmail(user.getEmail());
		if(userExists == true) {
			try {
				throw new EntityExistException("User already existst with email: " + user.getEmail());
			} catch (EntityExistException e) {
				e.printStackTrace();
				return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
			}
		}
		
		// Save User
		user = ((UserService)super.service).save(user);
		
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	@PostMapping(value = "/admin-save-all", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> saveAllAdmin(@RequestBody @Valid List<UserDTO> usersDto) {
		
		// Check If any User already exist
		List<User> users = new ArrayList<User>();
		for (UserDTO userDTO : usersDto) {
			try {
				User user = userDTO.transform(UserRole.ROLE_ADMIN);
				boolean userExists = ((UserService)super.service).existsByEmail(user.getEmail());
				if(userExists == true) {
					throw new EntityExistException("User already existst with email: " + user.getEmail());
				}
				users.add(user);
			} catch (EntityExistException e) {
				e.printStackTrace();
				return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
			}
			
		}
		
		// Save all Users
		users = ((UserService)super.service).saveAll(users);
		
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}
	
	/**
	 * Update User with specified UserID
	 * @return updatedUser
	 * @exception {@link rs.raf.chat_application_api.configuration.exception.EntityNotFoundException} - if User with specified UserID is not found in Database
	 */
	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Override
	public ResponseEntity<?> update(@RequestBody @Valid UserDTO userDto) {
		
		// Check if User with specified email exists
		User findUser = ((UserService)super.service).getById(userDto.getId());
		if(findUser == null) {
			throw new EntityNotFoundException("User not found with id: " + userDto.getId());	
		}
		
		// Update User
		findUser.setFirstname(userDto.getFirstname());
		findUser.setLastname(userDto.getLastname());
		findUser.setEmail(userDto.getEmail());
		findUser.setPassword(userDto.getPassword());
		
		findUser = ((UserService)super.service).update(findUser, findUser.getId());

		return new ResponseEntity<User>(findUser, HttpStatus.OK);
	}
	
	/**
	 * Adds selected user to logged in user friend list.
	 * @param loggedUserId - user that is currently logged in
	 * @param addUserId - user that we want to add to friend list
	 * @return loggedUser
	 */
	@PutMapping(value = "/add-to-friend-list/{loggedUserId}/{addUserId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> addUserToFriendList(@PathVariable("loggedUserId") Long loggedUserId, @PathVariable("addUserId") Long addUserId) {
		
		try {
			
			User addedUser = ((UserService)super.service).addUserToFriendList(loggedUserId, addUserId);
			
			// Transform User to UserDTO
			UserDTO userDto = new UserDTO(addedUser.getId(), addedUser.getFirstname(), addedUser.getLastname(), addedUser.getEmail(), addedUser.getPassword(), addedUser.getFriendList(), addedUser.getAuthorities(), addedUser.getIsOnline(), addedUser.getLastOnline());
			return new ResponseEntity<UserDTO>(userDto, HttpStatus.OK);
			
		} catch (UserNotFoundException e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		
	}
	
	@DeleteMapping(value = "/{userId}")
	@Override
	public ResponseEntity<?> delete(@PathVariable("userId") Long userId) {
		((UserService)super.service).delete(userId);
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	//----------------------------------------------------------------------------------------------------------------------
	// *** DEPRECATED FUNCTIONS ***
	//----------------------------------------------------------------------------------------------------------------------
	@Deprecated
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Override
	public ResponseEntity<?> save(@RequestBody @Valid UserDTO entity) {
		
		try {
			throw new UnsupportedFunctionException("UserController#save(User) method is not supported, instead use UserController#saveUser(User) or UserController#saveAdmin(Admin) method.");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
		}
		
	}
	
	@Deprecated
	@PostMapping(value = "/all", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Override
	public ResponseEntity<?> saveAll(@RequestBody @Valid List<UserDTO> entities) {
		
		try {
			throw new UnsupportedFunctionException("UserController#saveAll(User) method is not supported, instead use UserController#saveUser(User) or UserController#saveAdmin(Admin) method.");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
		}
		
	}
	//----------------------------------------------------------------------------------------------------------------------
	
}
