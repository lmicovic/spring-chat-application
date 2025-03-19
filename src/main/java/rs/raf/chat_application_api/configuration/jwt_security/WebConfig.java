package rs.raf.chat_application_api.configuration.jwt_security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	
	@Value(value = "${custom.variables.CorsPolicy.enable: false}")
	private boolean enableCorsFilter = false;
	
	/**
	 * Disables or Enables CORS Policy.<br>
	 * <b>[NOTE]:</b> enable CORS Policy in production Application
	 */
	public void addCorsMappings(CorsRegistry registry) {
		
		System.out.println(this.enableCorsFilter);
		
		if(this.enableCorsFilter == false) {						
			// Disable CORS Policy			
			registry.addMapping("/**")					
			  .allowedOrigins("*")		
			  .allowedMethods("GET", "POST", "PUT", "DELETE");
		}
		else if(this.enableCorsFilter == false) {
			// Enable CORS Policy
		}
		
	}
	
}
