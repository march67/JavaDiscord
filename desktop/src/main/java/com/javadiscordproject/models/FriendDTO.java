package com.javadiscordproject.models;

public class FriendDTO {
    private String username;
    private Long conversationId;

    public FriendDTO() {}

    public FriendDTO(String username, Long conversationId) {
        this.username = username;
        this.conversationId = conversationId;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }
    @Override
    public String toString() {
        return username;
    }
}