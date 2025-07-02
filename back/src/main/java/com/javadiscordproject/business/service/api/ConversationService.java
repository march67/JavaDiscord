package com.javadiscordproject.business.service.api;

import com.javadiscordproject.domain.model.ConversationModel;

public interface ConversationService {
	public void saveConversation(ConversationModel conversationModel);
	public ConversationModel findConversation(Long conversationId);
}
