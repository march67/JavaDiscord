package com.javadiscordproject.models;

public class FriendDTO {
    private String username;

    public FriendDTO() {}

    public FriendDTO(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    @Override
    public String toString() {
        return username;
    }
}