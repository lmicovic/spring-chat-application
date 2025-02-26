package rs.raf.chat_application_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rs.raf.chat_application_api.model.ChatNotification;

@Repository
public interface ChatNotificationRepository extends JpaRepository<ChatNotification, Long>{

}
