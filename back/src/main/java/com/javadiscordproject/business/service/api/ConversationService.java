package com.javadiscordproject.business.service.api;

import java.time.LocalDateTime;
import java.util.Optional;

import com.javadiscordproject.domain.model.ConversationModel;

public interface ConversationService {
	void createConversation(ConversationModel conversationModel);
	Optional<ConversationModel> findConversation(Long conversationId);
	public void updateLastMessageDateTime(Long conversationId);
}
