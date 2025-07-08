package com.javadiscordproject.models;

public class FriendAcceptedNotificationDTO {
    private String username;

    public FriendAcceptedNotificationDTO() {
    }

    public FriendAcceptedNotificationDTO(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}