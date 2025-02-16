package rs.raf.chat_application_api.configuration.jwt_security;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtTokenDTO implements Serializable {

	private static final long serialVersionUID = 1098316779781276440L;
	private String jwtToken;

}
