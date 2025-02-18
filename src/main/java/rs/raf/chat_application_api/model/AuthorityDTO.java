package rs.raf.chat_application_api.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorityDTO implements Serializable {

	private static final long serialVersionUID = 9032474720409446002L;
	
	private String authority;
	
}
