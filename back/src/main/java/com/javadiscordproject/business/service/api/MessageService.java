package com.javadiscordproject.business.service.api;

import java.util.List;

import com.javadiscordproject.domain.model.MessageModel;

public interface MessageService {
	void saveMessage(MessageModel messageModel);
	List<MessageModel> getMessagesByConversationId(Long conversationId);
}
