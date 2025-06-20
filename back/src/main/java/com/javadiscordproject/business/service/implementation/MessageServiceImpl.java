package com.javadiscordproject.business.service.implementation;

import java.util.List;

import org.springframework.stereotype.Service;

import com.javadiscordproject.business.service.api.ConversationService;
import com.javadiscordproject.business.service.api.MessageService;
import com.javadiscordproject.domain.model.MessageModel;
import com.javadiscordproject.infrastructure.persistence.repository.MessageRepository;

@Service
public class MessageServiceImpl implements MessageService {
	private MessageRepository messageRepository;
	private ConversationService conversationService;
	
	public MessageServiceImpl(MessageRepository messageRepository, ConversationService conversationService) {
		this.messageRepository = messageRepository;
		this.conversationService = conversationService;
	}

	@Override
	public void createMessage(MessageModel messageModel) {
		// create the message
		messageRepository.save(messageModel);
		
		// update the last message date of the conversation
		Long conversationId = messageModel.getConversationId();
		conversationService.updateLastMessageDateTime(conversationId);
	}

	@Override
	public List<MessageModel> getMessagesByConversationId(Long conversationId) {
		List<MessageModel> messages = messageRepository.findByConversationId(conversationId);
		return messages;
	}
}
