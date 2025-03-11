# Spring Chat Application

## Table of Contents
- [Overview](#overview)
	- [Frontend Application](#frontend-application)
- [Features](#features)
	- [Spring Security Authentication On-Off](#spring-security-authentication-on-off)
- [Dependencies](#dependencies)
- [How to Connect to WebSocket API](#how-to-connect-to-websocket-api)
	- [Java Clients](#java-clients)
	- [StandardWebSocketClient vs STOMPWebSocketClient](#standardwebsocketclient-vs-stompwebsocketclient)
	- [STOMPWebSocketClient](#stompwebsocketclient)
		- [Destination Subscription](#destination-subscription)
		- [Message Handling](#message-handling)
		- [Payload Type](#payload-type)
		- [Handle Exceptions](#handle-exceptions)
- [How to run](#how-to-run)

## Overview

The **Spring Chat Application** is a backend service built with [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html "Java 17") and [Spring Boot](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot/3.4.2 "Spring Boot") (version: 3.4.2) that facilitates real-time communication between users. This application leverages [WebSockets](https://developer.mozilla.org/en-US/docs/Web/API/WebSockets_API "WebSockets") for private user-to-user chat and incorporates various features to enhance user interaction.

Application is written in [Eclipse IDE](https://eclipseide.org/ "Eclipse IDE") as [Maven](https://www.simplilearn.com/tutorials/maven-tutorial/maven-project-in-eclipse "Maven") Project.

<br>

#### Frontend Application
Frontend Application is implemented in [JavaScript](https://www.w3schools.com/js/ "JavaScript") and [Angular17](https://angular.dev/ "Angular17") Framework and can be found at this link: https://github.com/lmicovic/angular-chat-application

## Features

- **Private User-to-User Chat**: Utilize [WebSocket](https://developer.mozilla.org/en-US/docs/Web/API/WebSockets_API "WebSocket") with a built-in message broker for seamless communication.
- **Message Persistence**: Users can view messages even when they are offline, implemented using [Spring JPA](https://spring.io/projects/spring-data-jpa "Spring JPA"), [Hibernate](https://hibernate.org/ "Hibernate") and [H2 In-memory Database](https://www.h2database.com/html/main.html "H2 In-memory Database").
- **User Authentication**: Secure user authentication using [Spring Security](https://spring.io/projects/spring-security "Spring Security") with [JWT Token](https://jwt.io/ "JWT Token") Authentication.
- **Online User Status**: Check online users and view the last online status of users.
- **User Management**: Block users and add them to a friend list.
- **Typing Indicator**: Real-time typing indicator to enhance user interaction.
- **Media Sharing**: Send images and files as messages.
- **Advance Logging:** application containts advance logging (applicaton.log, connected_users.log, subscribed_users.log, messages.log and console). Logs are stored at following path: `projectRoot\logs`

<br>

### Spring Security Authentication On-Off:
Spring Security Authentication can be turned on or of by defining following configuration in: `projectRoot\src\main\resources\application.properties`

- Turn on Spring Security Authentication:
`custom.variables.SpringSecurity.enable: true`

- Turn off Spring Security Authentication:
`custom.variables.SpringSecurity.enable: false`

## Dependencies

- [Spring Boot 3.4.2](https://spring.io/blog/2025/01/23/spring-boot-3-4-2-available-now "Spring Boot 3.4.2")
- [Spring Security](https://spring.io/projects/spring-security "Spring Security")
- [Spring JPA](https://spring.io/projects/spring-data-jpa "Spring JPA")
- [Spring Boot Validation](https://spring.io/guides/gs/validating-form-input "Spring Boot Validation")
- [Spring WebSocket](https://spring.io/guides/gs/messaging-stomp-websocket "Spring WebSocket")
- [JWT API](https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-api/0.12.6 "JWT API")
- [Jackson Databind](https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind "Jackson Databind")
- [Hibernate](https://hibernate.org/ "Hibernate")
- [H2 In-memory Database](https://www.h2database.com/html/main.html "H2 In-memory Database")
- [MapStruct](http://mapstruct.org/ "MapStruct")
- [Lombok](https://projectlombok.org/ "Lombok")
- [Maven](https://maven.apache.org/ "Maven")

## How to Connect to WebSocket API

### Java Clients:
Example how to use WebSocket API for Java Clients is at path:
`projectRoot\src\main\java\rs\raf\chat_application_api\configuration\websocket\client\client1.java`

##### Client1.java:
```java
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

@Component
public class Client1 {

    private static StompSession session;

    public static void main(String[] args) {
        connect();
    }

    private static void connect() {
        WebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSessionHandler sessionHandler = new ChatSessionHandlerClient1();
        try {
            session = stompClient.connect("ws://localhost:8080/ws", sessionHandler).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendMessage(ChatMessageDTO message) throws JsonProcessingException {
        if (session != null && session.isConnected()) {
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
        if (payload instanceof ChatMessageDTO) {
            System.err.println("[MESSAGE-RECEIVED]: " + payload);
        }
    }
}
```

#### StandardWebSocketClient vs STOMPWebSocketClient:
- ##### StandardWebSocketClient:
	- **Protocol:** Uses the WebSocket protocol, which provides full-duplex communication channels over a single TCP connection.
	- **Communication:** Allows for real-time data transfer between a client and server.
	- **Low-Level API:** Provides a lower-level API for sending and receiving messages, requiring developers to handle message framing and encoding.
	- **Use Cases:** Ideal for applications that require real-time updates, such as chat applications, live notifications, or streaming data.

- ##### STOMPWebSocketClient:
	- **Protocol:** STOMP (Simple Text Oriented Messaging Protocol) is an application-level protocol that uses WebSockets as a transport layer.
	- **Message-Oriented:** Adds a messaging layer on top of WebSockets, allowing for easier implementation of messaging patterns (e.g., pub/sub).
	- **Higher-Level API:** Provides a higher-level API with built-in support for message acknowledgment, headers, and message types, simplifying the development process.
	- **Use Cases:** Suitable for applications requiring complex messaging scenarios, such as enterprise messaging systems or applications needing to integrate with message brokers (e.g., ActiveMQ, RabbitMQ).
<br>

### StompSessionHandler:
The StompSessionHandler is an interface used in STOMP clients to manage the lifecycle and interactions of a STOMP session. It provides callback methods that allow developers to handle various events related to the STOMP session.

```java
class ChatSessionHandlerClient1 implements StompSessionHandler {
	//...
}
```
<br>

##### **Destination Subscription:**

Method afterConnected(StompSession, StompHeaders) is triggered after Client is connected to WebSocket API. It is usually used to set subscription for specific WebSocket destination by individual Client.
```java
@Override
public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
	session.subscribe("/user/client1/queue/messages", this);
}
```
<br>

##### **Message Handling:**

Method handleFrame(StompHeaders, Object) is used to handle incoming Messages from a STOMP broker.
```java
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
```
<br>

##### **Payload Type:**

Method getPayloadType(StompHeaders) is responsible for determining the type of the payload (the Message content) based on the headers of the incoming STOMP message.
```java
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
```
<br>

##### **Handle Exceptions:**

Method handleException(StompSession, StompCommand, StompHeaders, byte[], Throwable) is designed to handle exceptions that occur during the processing of STOMP messages.
 ```java
@Override
public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
	exception.printStackTrace();
	System.err.println(exception.getMessage());
}
```



## How to run:
This section will describe how to run application using [Eclispe IDE](https://eclipseide.org/ "Eclispe IDE").

1. Clone Project from GitHub Repository:
	- Open Terminal and type:
	`git clone https://github.com/lmicovic/spring-chat-application`
	
1. Install Lombok library on the PC:
	- Download Lombok library from: https://projectlombok.org/download
	- Install Lombok library:
		- Click on downloaded lombok.jar file and install.
		
1. Run Application in Eclise IDE:
Application is autoconfigured and ready to run.
	- Import project to E workspace.
	- Run Application by:
		- Right Click on Application.java file in workspace tree from Eclipse IDE.
		- Run as/ Java Application
	- Application will be started on the URL: http://localhost:8080/



------------



## Contributions

Other contributions are welcome! Feel free to open issues, submit pull requests, or suggest improvements.
