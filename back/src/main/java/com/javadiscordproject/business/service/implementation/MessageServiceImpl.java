package com.javadiscordproject.business.service.implementation;

import java.util.List;

import org.springframework.stereotype.Service;

import com.javadiscordproject.business.service.api.ConversationService;
import com.javadiscordproject.business.service.api.MessageService;
import com.javadiscordproject.business.service.api.UserConversationService;
import com.javadiscordproject.domain.model.MessageModel;
import com.javadiscordproject.domain.model.UserConversationId;
import com.javadiscordproject.infrastructure.persistence.repository.MessageRepository;

@Service
public class MessageServiceImpl implements MessageService {
	private MessageRepository messageRepository;
	private UserConversationService userConversationService;
	
	public MessageServiceImpl(MessageRepository messageRepository, UserConversationService userConversationService) {
		this.messageRepository = messageRepository;
		this.userConversationService = userConversationService;
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
		return messages;
	}
}
