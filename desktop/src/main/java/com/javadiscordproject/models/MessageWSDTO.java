package com.javadiscordproject.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageWSDTO {
    @JsonProperty("sender")
    private String sender;

    @JsonProperty("content")
    private String content;

    @JsonProperty("conversationId")
    private Long conversationId;

    @JsonProperty("timestamp")
    private String timestamp;

    public MessageWSDTO() {}

    public MessageWSDTO(String sender, String content, String timestamp, Long conversationId) {
        this.sender = sender;
        this.content = content;
        this.timestamp = timestamp;
        this.conversationId = conversationId;
    }

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}