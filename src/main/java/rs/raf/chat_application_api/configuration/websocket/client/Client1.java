package rs.raf.chat_application_api.configuration.websocket.client;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import rs.raf.chat_application_api.model.ChatMessageDTO;
import rs.raf.chat_application_api.model.ErrorMessageDTO;
import rs.raf.chat_application_api.model.User;

@Component
public class Client1 {
	
	private static StompSession session;
	
	public static void main(String[] args) {
		Client1.connect();
	}
	
	private static void connect() {
        WebSocketClient client = new StandardWebSocketClient();						// Create Standard WebSocket Client
        WebSocketStompClient stompClient = new WebSocketStompClient(client);		// Create STOMP WebSocket Client from Standard WebSocket Client
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());		// Enalbe WebSocket to serialize sending Messages(ChatMessage) to JSON.
        StompSessionHandler sessionHandler = new ChatSessionHandlerClient1();		// Define out Custom StompSessionHandler

        try {
            Client1.session = stompClient.connect("ws://localhost:8080/ws", sessionHandler).get();			// method initiates a connection to a WebSocket endpoint. This is typically the URL where the STOMP server is listening for incoming connections. 
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        //----------------------------------------------------------------
        // Example of sending a message
        //----------------------------------------------------------------
        try {
        	
        	User user1 = new User();
        	user1.setId(1l);
        	
        	User user2 = new User();
        	user1.setId(2l);
        	
        	ChatMessageDTO chatMessage = new ChatMessageDTO(1l, 2l, "Hello from Client1", new Date());
        	
			Client1.sendMessage(chatMessage);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
        
        // Keeps Client Alive
        while(true) {
        	
        }
        
    }
	
	public static void sendMessage(ChatMessageDTO message) throws JsonProcessingException {
        
		if (session != null && session.isConnected()) {
        	
        	 try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

        	session.send("/app/chat", message);
        }
    }
	
}

class ChatSessionHandlerClient1 implements StompSessionHandler {
    
	@Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        session.subscribe("/user/client1/queue/messages", this);
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        
    	if(payload instanceof ErrorMessageDTO) {
    		try {
    			ErrorMessageDTO errorMessageDTO = (ErrorMessageDTO) payload;
    			throw errorMessageDTO.getException();
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
    	}
    	else if(payload instanceof ChatMessageDTO) {
    		
			System.err.println("[MESSAGE-RECEIVED]: " + payload);

    	}
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        
    	List<String> messageTypes = headers.get("message-type");
    	if(messageTypes != null && messageTypes.contains("error")) {
    		return ErrorMessageDTO.class;
    	}
    	else if(messageTypes == null || messageTypes.isEmpty() || messageTypes.contains("chat")) {
    		return ChatMessageDTO.class;
    	}
    	
    	
    	return ChatMessageDTO.class;
    }

	@Override
	public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
		exception.printStackTrace();
		System.err.println(exception.getMessage());
	}

	@Override
	public void handleTransportError(StompSession session, Throwable exception) {
		
	}
}
