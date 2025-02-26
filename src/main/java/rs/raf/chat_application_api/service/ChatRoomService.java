package rs.raf.chat_application_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.raf.chat_application_api.model.ChatRoom;
import rs.raf.chat_application_api.model.User;
import rs.raf.chat_application_api.repository.ChatRoomRepository;

@Service
public class ChatRoomService {
	
	private ChatRoomRepository chatRoomRepository;
	
	@Autowired
	public ChatRoomService(ChatRoomRepository chatRoomRepositor) {
		this.chatRoomRepository = chatRoomRepositor;
	}
	
	public ChatRoom save(ChatRoom chatRoom) {
		return this.chatRoomRepository.save(chatRoom);
	}
	
	public ChatRoom getChatRoom(User userSender, User userReceiver) {
		
		String chatId = userSender.getId() + "_" + userReceiver.getId();
		ChatRoom chatRoom = this.chatRoomRepository.findByChatId(chatId);
		
		return chatRoom;
		
	}
	
}
