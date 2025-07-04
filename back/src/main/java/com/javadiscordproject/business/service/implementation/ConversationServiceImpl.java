package com.javadiscordproject.business.service.implementation;
import org.springframework.stereotype.Service;

import com.javadiscordproject.business.service.api.ConversationService;
import com.javadiscordproject.domain.model.ConversationModel;
import com.javadiscordproject.infrastructure.persistence.repository.ConversationRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ConversationServiceImpl implements ConversationService {
	private ConversationRepository conversationRepository;
	
	public ConversationServiceImpl(ConversationRepository conversationRepository) {
		this.conversationRepository = conversationRepository;
	}
	
	@Override
	public void saveConversation(ConversationModel conversationModel) {
		conversationRepository.save(conversationModel);
	}

	@Override
	public ConversationModel findConversation(Long conversationId) {
		return conversationRepository.findById(conversationId)
				.orElseThrow(() -> new EntityNotFoundException("Conversation not found with id = " + conversationId));
	}
}
