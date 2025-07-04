package com.javadiscordproject.services;

import com.javadiscordproject.models.MessageDTO;
import com.javadiscordproject.models.MessageWSDTO;

public class MessageConversionService {

    public MessageDTO convertWebSocketToDTO(MessageWSDTO wsMessage) {
        if (wsMessage == null) {
            return null;
        }

        MessageDTO dto = new MessageDTO();
        dto.setUsername(wsMessage.getSender());
        dto.setMessageContent(wsMessage.getContent());
        dto.setMessageDateTime(wsMessage.getTimestamp());

        return dto;
    }
}