package com.javadiscordproject.business.service.implementation;

import java.util.List;

import org.springframework.stereotype.Service;

import com.javadiscordproject.business.service.api.MessageService;
import com.javadiscordproject.domain.model.MessageModel;
import com.javadiscordproject.infrastructure.persistence.repository.MessageRepository;

@Service
public class MessageServiceImpl implements MessageService {
	private MessageRepository messageRepository;
	
	public MessageServiceImpl(MessageRepository messageRepository) {
		this.messageRepository = messageRepository;
	}

	@Override
	public void createMessage(MessageModel messageModel) {
		messageRepository.save(messageModel);
	}

	@Override
	public List<MessageModel> getMessagesByConversationId(Long conversationId) {
		List<MessageModel> messages = messageRepository.findByConversationId(conversationId);
		return messages;
	}
}
