package com.javadiscordproject.services;

import com.javadiscordproject.models.MessageDTO;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MessageDisplayService {
    private static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void displayMessage(MessageDTO message, VBox messageList, ScrollPane scrollPane, boolean isCurrentUser) {
        HBox messageGroup = new HBox();
        createUserMessage(message, messageGroup, isCurrentUser);
        messageList.getChildren().add(messageGroup);
        scrollPane.setVvalue(1.0);
    }

    private void createUserMessage(MessageDTO message, HBox messageGroup, boolean isCurrentUser) {
        messageGroup.getStyleClass().add(isCurrentUser ? "user-message-container" : "other-message-container");

        VBox contentBox = new VBox();
        contentBox.setSpacing(2);
        contentBox.getStyleClass().add(isCurrentUser ? "user-content" : "other-content");

        Label usernameLabel = new Label(message.getUsername());
        usernameLabel.getStyleClass().add(isCurrentUser ? "user-username" : "other-username");

        Label messageLabel = new Label(message.getMessageContent());
        messageLabel.getStyleClass().add("message-label");

        Label timeLabel = new Label(formatDateTime(message.getMessageDateTime()));
        timeLabel.getStyleClass().add("message-time");
        timeLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #888;");

        HBox messageContainer = new HBox();
        messageContainer.getStyleClass().add(isCurrentUser ? "user-message" : "other-message");
        messageContainer.getChildren().add(messageLabel);

        contentBox.getChildren().addAll(usernameLabel, messageContainer, timeLabel);
        messageGroup.getChildren().add(contentBox);
    }

    private String formatDateTime(String dateTimeString) {
        try {
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeString);
            return dateTime.format(DISPLAY_FORMATTER);
        } catch (Exception e) {
            return dateTimeString;
        }
    }
}