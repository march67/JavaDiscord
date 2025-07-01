package com.javadiscordproject.business.service.api;

import java.util.List;

import com.javadiscordproject.domain.model.UserConversationModel;

public interface UserConversationService {
	public void saveUserConversation(UserConversationModel userConversationModel);
	public void updateLastMessage(Long conversationId);
	public List<UserConversationModel> findByUserId(Long userId);
}
