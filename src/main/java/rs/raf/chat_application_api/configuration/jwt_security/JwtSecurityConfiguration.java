package rs.raf.chat_application_api.configuration.jwt_security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import rs.raf.chat_application_api.repository.UserRepository;
import rs.raf.chat_application_api.service.UserService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class JwtSecurityConfiguration {

	/**
	 * Variable that specifies if Spring Security Authentication is Enabled/Disabled.<br><br>
	 * 
	 * This can be specified in a two places:
	 * <ul>
	 * 		<li>default value - Enabled</li>
	 * 		<li>application.properties file - define this setting custom.variables.SpringSecurity: true or custom.variables.SpringSecurity.enable: false</li>
	 * 		<li>explicitly in JwtSecurityConfiguration class - by setting enableSpringSecurity = true or false</li>
	 * </ul>
	 * 
	 * <b>Possible values:</b><br>
	 * <ul>
	 * 		<li>enableSpringSecurity = true - Enables Spring Security Authentication</li>
	 * 		<li>enableSpringSecurity = false - Disables Spring Security Authentication</li>
	 * </ul>
	 */
	@Value(value = "${custom.variables.SpringSecurity.enable: true}")
	private boolean enableSpringSecurity = false;
	
	@Autowired
	private JwtAuthFilter authFilter;
	
	@Autowired
	private UserRepository userRepository;
	
	// User Creation
	@Bean
	public UserService userDetailsService() {	
		return new UserService(this.userRepository);
	}
	
	
	
	// Configuring HttpSecurity
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, UserRepository userRepository) throws Exception {
		
		if(this.enableSpringSecurity == false) {
			
			return http.csrf(csrf -> csrf.disable())
					.authorizeHttpRequests((auth) -> auth.anyRequest().permitAll())
					.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	                // Enable h2-console in Spring Security on URL: /h2-console
	                .headers((headers) -> {
	                	headers.frameOptions((frame) -> {
	                		frame.sameOrigin();
	                	});
	                })
	                .build();			
		}
		


    	return http.csrf(csrf -> csrf.disable())
        		
                .authorizeHttpRequests(auth -> auth.requestMatchers("/h2-console/**", "/auth/welcome", "/auth/generateToken", "/auth/signin", "/email-exists/{email}").permitAll())		// URL Does not need Authorizaton
                .authorizeHttpRequests((auth) -> auth.anyRequest().authenticated())																					// All Other URLS need Authorization
//                .authorizeHttpRequests(auth -> auth.requestMatchers("/auth/user/**",  "/movie/**").authenticated())
//                .authorizeHttpRequests(auth -> auth.requestMatchers("/auth/admin/**").authenticated())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(this.authenticationProvider())
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                
                // Enable h2-console in Spring Security on URL: /h2-console
                .headers((headers) -> {
                	headers.frameOptions((frame) -> {
                		frame.sameOrigin();
                	});
                })
                .build();
	}

	// Enable H2-Console in Spring Security
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().requestMatchers(new AntPathRequestMatcher("/h2-console/**"));
	}

	// Password Encoding
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(this.userDetailsService());
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

}