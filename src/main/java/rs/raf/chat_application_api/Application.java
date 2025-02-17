package rs.raf.chat_application_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//--------------------------------------------------------
// Requirements
//--------------------------------------------------------
// Install Lombok - download lombok .jar

//--------------------------------------------------------
// Guide:
//--------------------------------------------------------
//
// 1) Enable/Disable Spring Security:   
//
//			- JwtSecurityConfiguration#enableSpringSecurity = true or false
//			- application.properties file:   custom.variables.SpringSecurity.enable: true or custom.variables.SpringSecurity.enable: false
//
//
//	2) Welcome Controller:  utl: localhost:8080/auth/welcome
//
//		// UserAuthenticationController#welcome() method - program test route, this route is not protected by Authentication.
//
//	
//	
//	3) GenerateToken:		utl: localhost:8080/auth/generateToken
//
//		// This route is used for User Authentication
//		// This route is not protected by Spring Boot Authentication, so any one can call it.
//		// This is Route that is used for User SignIn, User has to send JSON(email, password), if User (email, password) is correct route returns JWT Token.
//
//	
//	4) All other Routes are protected by Spring Boot Authentication:
//
//		1) First User needs to get Authenticated by calling: 	localhost:8080/auth/generateToken
//		2) Then if User is Authenticated Successfully, route returns JWT Token. That JWT Token user has to use in Every other HTTP Request in order to get Authenticated.
//
//
//
// 5) Other Routes in API:
//
//		- Authentication Controller:
//	
//			- Welcome:				GET  http://localhost:8080/auth/welcome
//			- GenerateToken:		POST http://localhost:8080/auth/generateToken		- send JSON Format
//
//
//
//		- User Controller:
//
//			- GetAllUsers:			GET  http://localhost:8080/user
// 			- GetUserById:			GET  http://localhost:8080/user/{userId}
//			- saveUser:				POST 
//
//
//
//
//--------------------------------------------------------


@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
