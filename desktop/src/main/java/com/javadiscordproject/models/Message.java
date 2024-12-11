package com.javadiscordproject.models;

public class Message {
    private final String content;
    private final String sender;
    private final boolean isUser;

    public Message(String content, String sender, boolean isUser) {
        this.content = content;
        this.sender = sender;
        this.isUser = isUser;
    }

    public String getContent() {
        return content;
    }

    public String getSender() {
        return sender;
    }

    public boolean isUser() {
        return isUser;
    }

    public boolean isSystem() {
        return "Système".equals(sender);
    }
}