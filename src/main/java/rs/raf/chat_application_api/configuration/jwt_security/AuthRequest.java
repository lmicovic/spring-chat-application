package rs.raf.chat_application_api.configuration.jwt_security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
	
	private String email;
	private String password;
	
}