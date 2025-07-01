package com.javadiscordproject.business.service.implementation;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.javadiscordproject.business.service.api.UserConversationService;
import com.javadiscordproject.business.service.api.UserService;
import com.javadiscordproject.domain.model.UserConversationModel;
import com.javadiscordproject.infrastructure.persistence.repository.UserConversationRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserConversationServiceImpl implements UserConversationService {
	UserConversationRepository userConversationRepository;
	UserService userService;
	
	public UserConversationServiceImpl(UserConversationRepository userConversationRepository, UserService userService) {
		this.userConversationRepository = userConversationRepository;
		this.userService = userService;
	}
	
	@Override
	public void saveUserConversation(UserConversationModel userConversationModel) {
		userConversationRepository.save(userConversationModel);
	}

	@Override
	public void updateLastMessage(Long conversationId) {
		List<UserConversationModel> userConversations = userConversationRepository.findByConversationId(conversationId);
		if (userConversations.isEmpty()) {
			throw new EntityNotFoundException("UserConversation not found with conversation id = " + conversationId);
		}
		
		for (UserConversationModel entity : userConversations) {
			entity.updateLastMessageDateTime();
			userConversationRepository.save(entity);
		}
	}

	@Override
	public List<UserConversationModel> findByUserId(Long userId) {
		return userConversationRepository.findByUserId(userId);
	}

	@Override
	public Long findConversationIdByUsernames(String username1, String username2) {
		Long userId1 = userService.findUserByUsername(username1).getId();
		Long userId2 = userService.findUserByUsername(username2).getId();
		List<UserConversationModel> userConversation1 = userConversationRepository.findByUserId(userId1);
		List<UserConversationModel> userConversation2 = userConversationRepository.findByUserId(userId2);
		
	    Set<Long> conversationIds1 = userConversation1.stream()
	            .map(uc -> uc.getId().getConversationId())
	            .collect(Collectors.toSet());
	        
        Set<Long> conversationIds2 = userConversation2.stream()
	            .map(uc -> uc.getId().getConversationId())
	            .collect(Collectors.toSet());
        
        // Only shared conversation ID remains
        conversationIds1.retainAll(conversationIds2);
        
        // Filter userConversation that have only two participant
        return conversationIds1.stream()
            .filter(conversationId -> {
                long participantCount = userConversationRepository.countByConversationId(conversationId);
                return participantCount == 2;
            })
            .findFirst().orElseThrow();
	}

}
