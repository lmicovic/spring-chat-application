package rs.raf.chat_application_api.configuration.websocket.websocket_interceptor;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

public class WebSocketMessageInterceptor implements ChannelInterceptor {

	private static StompHeaderAccessor sha;
	
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		
		WebSocketMessageInterceptor.sha = StompHeaderAccessor.wrap(message);
		
		if(sha.getCommand() == StompCommand.SUBSCRIBE) {
			String sessionId = sha.getSessionId();
			String destination = sha.getDestination();
			System.err.println("[USER-SUBSCRIBED]: {sessionId: " + sessionId + ", destination: " + destination + "}");
		}
		
		return ChannelInterceptor.super.preSend(message, channel);
	}
	
}
