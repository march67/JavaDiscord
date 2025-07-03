package com.javadiscordproject.business.service.api;

import java.util.List;

import com.javadiscordproject.domain.model.MessageModel;
import com.javadiscordproject.presentation.dto.Chat.MessageDTO;
import com.javadiscordproject.presentation.dto.Chat.NotificationDTO;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

public interface MessageService {
	void saveMessage(MessageModel messageModel);
	List<MessageModel> getMessagesByConversationId(Long conversationId);
	MessageDTO handleMessage(MessageDTO message, SimpMessageHeaderAccessor headerAccessor);
	public MessageDTO handleMessageByConversation(@DestinationVariable String id, MessageDTO message, SimpMessageHeaderAccessor headerAccessor);
	public NotificationDTO handleNotification(NotificationDTO notification);
}
