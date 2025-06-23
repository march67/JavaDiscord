package com.javadiscordproject.business.service.api;

import com.javadiscordproject.domain.model.UserConversationModel;

public interface UserConversationService {
	public void saveUserConversation(UserConversationModel userConversationModel);
	public void updateLastMessage(Long conversationId);
}
