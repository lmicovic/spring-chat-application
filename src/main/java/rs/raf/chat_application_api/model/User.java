package rs.raf.chat_application_api.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.raf.chat_application_api.configuration.enums.UserRole;

@Entity(name = "`USERS`")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable, UserDetails {

	private static final long serialVersionUID = 7757961288363404234L;
	
//	private static final PasswordEncoder encoder = new BCryptPasswordEncoder();
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Setter(AccessLevel.NONE)									// Prevents Lombok to create setter for id.
	private Long id;
	
	@Column(name = "firstname", nullable = false)
	@NotBlank(message = "Firstname is mandatory")
	private String firstname;
	
	@Column(name = "lastname", nullable = false)
	@NotBlank(message = "Lastname is mandatory")
	private String lastname;
	
	@Column(name = "email", nullable = false, unique = true)
	@NotBlank(message = "E-mail is mandatory")
	@Email
//	@UniqueElements()
	private String email;
	
	@Column(name = "passowrd", nullable = false)
//	@Transient
//	@NotBlank(message = "Password is mandatory")
	@JsonIgnore
//	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Password must be at least 8 characters long, include at least one uppercase letter, one lowercase letter, one digit, and one special character.")
	private String password;
	
//	@Column(name = "encriptedPassword")
//	private String encriptedPassword;
	
	@Column(name = "authorities")
	private Collection<? extends GrantedAuthority> authorities;
	
//	/**
//	 * User sentMessages is always null, if you want to get User's all sent messages use findByUserSenderId() {@link rs.raf.chat_application_api.repository.MessageRepository#findByUserSenderId(Long)} method from MessageRepository.
//	 */
//	@OneToMany(mappedBy = "userSender", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//	@JsonBackReference
//	@JsonIgnore
//	private Set<Message> sentMessages;
//	
//	/**
//	 * User receivedMessages is always null, if you want to get User received messages use findByUserReceiverId() {@link rs.raf.chat_application_api.repository.MessageRepository#findByUserReceiverId(Long)} method from MessageRepository.
//	 */
//	@OneToMany(mappedBy = "userReceiver", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//	@JsonBackReference
//	@JsonIgnore
//	private Set<Message> receivedMessages;
	

//	private String image;				// add Image later
//	private List<String> chat			// add Chat later
	
	/**
	 * Constructor creates User with following predefined atributes:
	 * <ul>
	 * 		<li><b>authorities - </b> predefined User Role is "ROLE_USER"</li>
	 * </ul>
	 * 
	 * @param id
	 * @param firstname
	 * @param lastname
	 * @param email
	 * @param password
	 */
	public User(Long id, String firstname, String lastname, String email, String password) {
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.password = password;
//		this.encriptedPassword = encoder.encode(password);
		
		Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		
        this.authorities = authorities;
	}
	
	/**
	 * Constructor creates User with following predefined atributes:
	 * 
	 * <ul>
	 * 		<li><b>authorities - </b> predefined User Role is "ROLE_USER"</li>
	 * </ul>
	 * 
	 * @param firstname
	 * @param lastname
	 * @param email
	 * @param password
	 */
	public User(String firstname, String lastname, String email, String password) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.password = password;
//		this.encriptedPassword = encoder.encode(password);
		
		Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		
        this.authorities = authorities;
	}
	
	/**
	 * 
	 * Constructor single USER_ROLE attribute.
	 * 
	 * @param firstname
	 * @param lastname
	 * @param email
	 * @param password
	 * @param role
	 */
	public User(String firstname, String lastname, String email, String password, UserRole role) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.password = password;
		
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(role.getRoleDescription()));
		
		this.authorities = authorities;
		
	}
	
	/**
	 * Constructor that accepts USER_ROLES as Array.
	 * 
	 * @param firstname
	 * @param lastname
	 * @param email
	 * @param password
	 * @param roles
	 */
	public User(String firstname, String lastname, String email, String password, List<UserRole> roles) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.password = password;
		
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		for (UserRole role : roles) {
			authorities.add(new SimpleGrantedAuthority(role.getRoleDescription()));
		}
		
		this.authorities = authorities;
		
	}
	

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@JsonIgnore
	@Override
	public String getUsername() {
		return this.email;
	}
	
	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
		
}
