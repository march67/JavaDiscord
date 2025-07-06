package com.javadiscordproject.services.api.message;

import com.javadiscordproject.models.MessageDTO;
import java.util.List;

public interface IMessageService {
    List<MessageDTO> getConversationMessages(Long conversationId);
}