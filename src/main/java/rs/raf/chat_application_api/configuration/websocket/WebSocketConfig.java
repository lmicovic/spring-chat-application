package rs.raf.chat_application_api.configuration.websocket;

import java.util.List; 

import org.apache.tomcat.util.http.fileupload.util.mime.MimeUtility;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.DefaultContentTypeResolver;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import com.fasterxml.jackson.databind.ObjectMapper;
import rs.raf.chat_application_api.configuration.websocket.websocket_interceptor.WebSocketMessageInterceptor;

/**
 * Application WebSocket Configuration Class.
 * This Class is used to setup and configure WebSocket support in Spring Boot.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	
	/**
	 * This method is used to Configure Message Broker that handles messaging between clients and the server.<br>
	 * It allows you to specify which prefixes should be used for different types of Message Destinations. For example, you might have prefixes for application-level Messages and those intended for the Broker.
	 */
	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/user");											// Configures a built-in Message Broker with one destination for sending and receiving messages.
		config.setApplicationDestinationPrefixes("/app");							// If you set the prefix to /app, any message sent to a destination that starts with /app will be routed to the application for processing. If a Client sends a Message to "/app/send/message" This message will be handled by a method annotated with @MessageMapping("/send/message") in ChatController
		config.setUserDestinationPrefix("/user");									// User destination prefix /user is used by ConvertAndSendToUser method of SimpleMessagingTemplate to prefix all user-specific destinations with "/user".
	}
	
	/**
	 *This method is used to configure and register the STOMP endpoints that Clients will use to connect to the WebSocket Server.
	 */
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {			 
		registry.addEndpoint("ws")													// This specifies a URL endpoint (in this case, /ws) that clients will use to initiate a WebSocket connection to Server			
				.setAllowedOrigins("*");											// This	is used to configure CORS Settings for the registered WebSocket endpoint. "*" means that all origins are allowed to connect to the WebSocket endpoint
//				.withSockJS();														// Enables SockJS support for a registered WebSocket endpoint, so that alternative messaging options may be used if WebSockets are not available
	}
	
	/**
	 * This method allows you to customize the list of message converters used by the Spring framework for converting messages in a messaging context (like WebSockets or messaging templates).
	 * @return false - method returns false, which indicates that the default message converters should not be overridden. If it returned true, it would replace the existing converters with the ones provided in this method.  
	 */
	@Override
	public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
		
		DefaultContentTypeResolver resolver = new DefaultContentTypeResolver();
		resolver.setDefaultMimeType(MimeTypeUtils.APPLICATION_JSON);
		
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();			// This converter is used for converting between Java objects and JSON using the Jackson library.
		converter.setObjectMapper(new ObjectMapper());												// ObjectMapper() is used to handle Serialization and Deserializetion of Java Object to and from JSON format.
		converter.setContentTypeResolver(resolver);													// The main purpose of a ContentTypeResolver is to resolve the content type of a message so that the appropriate message converter can be used to process that message.
		
		messageConverters.add(converter);															//  MappingJackson2MessageConverter is added to the list of message converters provided by Spring. This means that Spring will use this converter for message handling where applicable.
		
		return false;																				// The method returns false, which indicates that the default message converters should not be overridden. If it returned true, it would replace the existing converters with the ones provided in this method.
		
	}
	
	
	/**
	 * Create STOMP Message Interceptior, StompMessageInterceptor methods will be called every time Message arives or sent from Server.
	 */
	@Bean
    public ChannelInterceptor stompMessageInterceptor() {
        return new WebSocketMessageInterceptor();
    }
	
	/**
	 * Register STOMP Message Interceptor for incoming messages.
	 */
	@Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(this.stompMessageInterceptor());
    }
	
	/**
	 * Register STOMP Message Interceptor for outgoing messages. 
	 */
	@Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {

    }
	
}
