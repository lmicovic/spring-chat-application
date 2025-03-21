package rs.raf.chat_application_api.model;

import java.io.Serializable; 
import java.util.Collection;
import java.util.Date;
import org.springframework.security.core.GrantedAuthority;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.raf.chat_application_api.configuration.enums.UserRole;

/**
 * This class is used as Data Transfer Object, only to accepts User JSON Object in HTTP POST Request.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements Serializable{
	
	private static final long serialVersionUID = 2425307822154196497L;
	
	private Long id;
	
	@NotBlank(message = "First name is mandatory")
	private String firstname;

	@NotBlank(message = "Lastname is mandatory")
	private String lastname;

	@NotBlank(message = "Email is mandatory")
	@Email
	private String email;

	@NotBlank(message = "Passsword is mandatory")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Password must be at least 8 characters long, include at least one uppercase letter, one lowercase letter, one digit, and one special character.")
	private String password;
	
	private Collection<? extends GrantedAuthority> authorities;
	
	private Boolean isOnline;
	
	private Date lastOnline;
	
	public UserDTO(Long id, String firstname, String lastname, String email, String password) {
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.password = password;
	}
	
	public UserDTO(Long id, String firstname, String lastname, String email, String password, Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.password = password;
		this.isOnline = false;
		this.lastOnline = new Date();
		this.authorities = authorities;
	}
	
	/**
	 * Used to create User object from UserDTO object
	 * @param userRole - ROLE_USER, ROLE_ADMIN, ROLE_GUEST
	 * @return user - user object
	 */
	public User transform(UserRole userRole) {
		User user = new User(this.firstname, this.lastname, this.email, this.password, userRole);
		return user;
	}
	
}
