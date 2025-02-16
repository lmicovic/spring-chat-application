package rs.raf.chat_application_api.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.AssertFalse.List;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity(name = "`user`")
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
	@NotBlank(message = "Password is mandatory")
//	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Password must be at least 8 characters long, include at least one uppercase letter, one lowercase letter, one digit, and one special character.")
	private String password;
	
//	@Column(name = "encriptedPassword")
//	private String encriptedPassword;
	
	@Column(name = "authorities")
	private Collection<? extends GrantedAuthority> authorities;
	
	@Column(name = "active")
	private Boolean active;

//	private String image;				// add Image later
//	pricate List<String> chat			// add Chat later
	
	public User(String firstname, String lastname, String email, String password) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.password = password;
//		this.encriptedPassword = encoder.encode(password);
		this.active = true;
		
		Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		
        this.authorities = authorities;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
		
}
