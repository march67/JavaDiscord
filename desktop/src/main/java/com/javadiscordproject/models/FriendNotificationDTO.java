package com.javadiscordproject.models;

public class FriendNotificationDTO {
    private String username_requester;
    private String username_receiver;

    public FriendNotificationDTO() {}

    public FriendNotificationDTO(String username_requester, String username_receiver) {
        this.username_requester = username_requester;
        this.username_receiver = username_receiver;
    }

    public String getUsername_requester() {
        return username_requester;
    }

    public void setUsername_requester(String username_requester) {
        this.username_requester = username_requester;
    }

    public String getUsername_receiver() {
        return username_receiver;
    }

    public void setUsername_receiver(String username_receiver) {
        this.username_receiver = username_receiver;
    }

    @Override
    public String toString() {
        return "FriendNotificationDto{" +
                "username_requester='" + username_requester + '\'' +
                ", username_receiver='" + username_receiver + '\'' +
                '}';
    }
}