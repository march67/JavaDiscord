package com.javadiscordproject.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "chat_message")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationMessageId;
    private String sender;
    private String content;
    private String timestamp;

    public ChatMessage() {
    }

    public ChatMessage(String sender, String content, String timestamp) {
        super();
        this.sender = sender;
        this.content = content;
        this.timestamp = timestamp;
    }

    // Getters et setters

    public Long getApplicationMessageId() {
        return this.applicationMessageId;
    }

    public void setApplicationMessageId(Long applicationMessageId) {
        this.applicationMessageId = applicationMessageId;
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
}

