package com.javadiscordproject.business.service.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.javadiscordproject.business.service.api.UserConversationService;
import com.javadiscordproject.domain.model.UserConversationModel;
import com.javadiscordproject.infrastructure.persistence.repository.UserConversationRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserConversationServiceImpl implements UserConversationService {
	UserConversationRepository userConversationRepository;
	
	public UserConversationServiceImpl(UserConversationRepository userConversationRepository) {
		this.userConversationRepository = userConversationRepository;
	}
	
	@Override
	public void saveUserConversation(UserConversationModel userConversationModel) {
		userConversationRepository.save(userConversationModel);
	}

	@Override
	public void updateLastMessage(Long conversationId) {
		List<UserConversationModel> userConversations = userConversationRepository.findByConversationId(conversationId);
		
		for (UserConversationModel entity : userConversations) {
			entity.updateLastMessageDateTime();
			userConversationRepository.save(entity);
		}
	}

}
