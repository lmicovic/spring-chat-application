package rs.raf.chat_application_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.raf.chat_application_api.model.ChatNotification;
import rs.raf.chat_application_api.repository.ChatNotificationRepository;

@Service
public class ChatNotificationService {
	
	private ChatNotificationRepository chatNotificationRepository;
	
	@Autowired
	public ChatNotificationService(ChatNotificationRepository chatNotificationRepository) {
		this.chatNotificationRepository = chatNotificationRepository;
	}
	
	public ChatNotification save(ChatNotification chatNotification) {
		return this.chatNotificationRepository.save(chatNotification);
	}
	
}
