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

}