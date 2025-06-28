package com.javadiscordproject.business.service.implementation;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.javadiscordproject.business.service.api.ConversationService;
import com.javadiscordproject.domain.model.ConversationModel;
import com.javadiscordproject.infrastructure.persistence.repository.ConversationRepository;

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
	public Optional<ConversationModel> findConversation(Long conversationId) {
		Optional<ConversationModel> result = conversationRepository.findById(conversationId);
		return result;
	}
}
