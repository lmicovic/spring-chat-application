package rs.raf.chat_application_api.configuration.jwt_security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import rs.raf.chat_application_api.model.User;
import rs.raf.chat_application_api.model.UserDTO;
import rs.raf.chat_application_api.service.UserService;

/**
 * Controller for User Authentication
 * @author lukam
 *
 */
@RestController
@RequestMapping("/auth")
public class UserAuthenticationController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Testing route, this route can be called by unauthenticated users.
     */
    @GetMapping(value = "/welcome", produces = MediaType.TEXT_PLAIN_VALUE)
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }
    
    /**
     * Authenticates User based on provided AuthRequest Credentials <b>(email, password)</b>.<br>
     * If user is Authenticated Successfully, then JWT Token is generated for specific User.
     * 
     * @param authRequest - User's email and password
     * @return jwtToken
     */
    @PostMapping(value = "/generateToken", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
    	
    	// Check If Email exists
    	if(userService.existsByEmail(authRequest.getEmail()) == false) {
    		return new ResponseEntity<String>("Wrong Email", HttpStatus.UNAUTHORIZED);
    	}
    	
    	// Check Password
    	UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword());
    	Authentication authentication = null;
    	try {
    		authentication = authenticationManager.authenticate(authenticationToken);
		} catch (BadCredentialsException e) {
//			e.printStackTrace();
			return new ResponseEntity<String>("Wrong password", HttpStatus.UNAUTHORIZED);
		}
    	
    	// If all ok -> generate JWT Token
        if (authentication.isAuthenticated()) {
        	
        	JwtTokenDTO jwtToken = new JwtTokenDTO(jwtService.generateToken(authRequest.getEmail())); 
        	
            return new ResponseEntity<JwtTokenDTO>(jwtToken, HttpStatus.OK);
        } else {
            throw new UsernameNotFoundException("invalid user request ");
        }
        
    }
    
    @PostMapping(value = "/signin", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> signin(@RequestBody UserDTO userDTO) {
    	
    	
    	User user = new User(userDTO.getFirstname(), userDTO.getLastname(), userDTO.getEmail(), userDTO.getPassword());
    	user = this.userService.save(user);
    	
    	userDTO = new UserDTO(user.getId(), user.getFirstname(), user.getLastname(), user.getEmail(), user.getPassword());
    	
    	return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);
    }
    
    @GetMapping(value = "/email-exists/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> emailExists(@PathVariable("email") String email) {
    	boolean exists = this.userService.existsByEmail(email);
    	return new ResponseEntity<Boolean>(exists, HttpStatus.OK);
    }
    
}