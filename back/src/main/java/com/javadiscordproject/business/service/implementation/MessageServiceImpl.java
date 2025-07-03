package com.javadiscordproject.business.service.implementation;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import com.javadiscordproject.domain.model.ConversationModel;
import com.javadiscordproject.domain.model.UserModel;
import com.javadiscordproject.infrastructure.persistence.repository.ConversationRepository;
import com.javadiscordproject.infrastructure.persistence.repository.UserRepository;
import com.javadiscordproject.presentation.dto.Chat.MessageDTO;
import com.javadiscordproject.presentation.dto.Chat.NotificationDTO;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.javadiscordproject.business.service.api.ConversationService;
import com.javadiscordproject.business.service.api.MessageService;
import com.javadiscordproject.business.service.api.UserConversationService;
import com.javadiscordproject.domain.model.MessageModel;
import com.javadiscordproject.domain.model.UserConversationId;
import com.javadiscordproject.infrastructure.persistence.repository.MessageRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class MessageServiceImpl implements MessageService {
	private MessageRepository messageRepository;
	private UserConversationService userConversationService;
	private UserRepository userRepository;
	private ConversationRepository conversationRepository;
	
	public MessageServiceImpl(MessageRepository messageRepository, UserConversationService userConversationService, UserRepository userRepository, ConversationRepository conversationRepository) {
		this.messageRepository = messageRepository;
		this.userConversationService = userConversationService;
		this.userRepository = userRepository;
		this.conversationRepository = conversationRepository;
	}

	@Override
	public void saveMessage(MessageModel messageModel) {
		// create the message
		messageRepository.save(messageModel);
		
		// update the last message date of the conversation
		Long conversationId = messageModel.getConversation().getId();
		userConversationService.updateLastMessage(conversationId);
	}

	@Override
	public List<MessageModel> getMessagesByConversationId(Long conversationId) {
		List<MessageModel> messages = messageRepository.findByConversationId(conversationId);
		if (messages.isEmpty()) {
			throw new EntityNotFoundException("Not message found at conversation id = " + conversationId);
		}
		
		return messages;
	}

	@Override
	public MessageDTO handleMessage(MessageDTO message, SimpMessageHeaderAccessor headerAccessor) {
		Principal user = headerAccessor.getUser();
		if (user != null) {
			message.setSender(user.getName());
		}

		message.setTimestamp(LocalDateTime.now().toString());

		saveMessageToDatabase(message);
		return message;
	}

	private void saveMessageToDatabase(MessageDTO messageDTO) {
		MessageModel message = new MessageModel();

		UserModel user = userRepository.findByUsername(messageDTO.getSender());

		ConversationModel conversation = conversationRepository.findById(Long.parseLong(messageDTO.getConversationId()))
				.orElseThrow(() -> new RuntimeException("Conversation non trouvée"));

		message.setUser(user);
		message.setConversation(conversation);
		message.setMessageContent(messageDTO.getContent());

		if (messageDTO.getTimestamp() != null) {
			message.setCreatedAt(LocalDateTime.parse(messageDTO.getTimestamp()));
		}

		messageRepository.save(message);
	}

	@Override
	public MessageDTO handleMessageByConversation(
			@DestinationVariable String id,
			MessageDTO message,
			SimpMessageHeaderAccessor headerAccessor
	) {
		Principal user = headerAccessor.getUser();
		if (user != null) {
			message.setSender(user.getName());
		}

		message.setTimestamp(LocalDateTime.now().toString());
		message.setConversationId(id);
		saveMessageToDatabase(message);
		return message;
	}

	@Override
	public NotificationDTO handleNotification(NotificationDTO notification) {
		notification.setTimestamp(LocalDateTime.now().toString());

		System.out.println("Notification: " + notification.getTitle() + " - " + notification.getContent());

		return notification;
	}
}
