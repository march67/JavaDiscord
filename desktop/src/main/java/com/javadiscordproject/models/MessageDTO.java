package com.javadiscordproject.models;

import com.fasterxml.jackson.annotation.JsonProperty;
public class MessageDTO {

    @JsonProperty("username")
    private String username;

    @JsonProperty("messageContent")
    private String messageContent;

    @JsonProperty("messageDateTime")
    private String messageDateTime;

    public MessageDTO() {
    }

    public MessageDTO(String username, String messageContent, String messageDateTime) {
        this.username = username;
        this.messageContent = messageContent;
        this.messageDateTime = messageDateTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getMessageDateTime() {
        return messageDateTime;
    }

    public void setMessageDateTime(String messageDateTime) {
        this.messageDateTime = messageDateTime;
    }

    @Override
    public String toString() {
        return "MessageDTO{" +
                "username='" + username + '\'' +
                ", messageContent='" + messageContent + '\'' +
                ", messageDateTime=" + messageDateTime +
                '}';
    }
}