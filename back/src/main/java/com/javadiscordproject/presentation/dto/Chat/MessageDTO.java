package com.javadiscordproject.presentation.dto.Chat;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

public class MessageDTO {

    private String sender;
    private String content;
    private String conversationId;
    private String timestamp;

    public MessageDTO() {
    }

    public MessageDTO(String sender, String content, String timestamp, String conversationId) {
        super();
        this.sender = sender;
        this.content = content;
        this.timestamp = timestamp;
        setConversationId(conversationId);
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
