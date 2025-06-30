package com.javadiscordproject.business.service.api;

import java.util.Optional;

import com.javadiscordproject.domain.model.ConversationModel;

public interface ConversationService {
	public void saveConversation(ConversationModel conversationModel);
	public ConversationModel findConversation(Long conversationId);
}
