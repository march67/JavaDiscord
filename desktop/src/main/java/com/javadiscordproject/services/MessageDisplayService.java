package com.javadiscordproject.services;

import com.javadiscordproject.models.Message;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MessageDisplayService {
    public void displayMessage(Message message, VBox messageList, ScrollPane scrollPane) {
        HBox messageGroup = new HBox();

        if (message.isSystem()) {
            createSystemMessage(message, messageGroup);
        } else {
            createUserMessage(message, messageGroup);
        }

        messageList.getChildren().add(messageGroup);
        scrollPane.setVvalue(1.0);
    }

    private void createSystemMessage(Message message, HBox messageGroup) {
        messageGroup.getStyleClass().add("system-message-container");

        Label messageLabel = new Label(message.getContent());
        messageLabel.getStyleClass().add("message-label");

        HBox messageContainer = new HBox();
        messageContainer.getStyleClass().add("system-message");
        messageContainer.getChildren().add(messageLabel);

        messageGroup.getChildren().add(messageContainer);
    }

    private void createUserMessage(Message message, HBox messageGroup) {
        messageGroup.getStyleClass().add(message.isUser() ? "user-message-container" : "other-message-container");

        VBox contentBox = new VBox();
        contentBox.setSpacing(2);
        contentBox.getStyleClass().add(message.isUser() ? "user-content" : "other-content");

        // Création du label pour le nom d'utilisateur
        Label usernameLabel = new Label(message.getSender());
        usernameLabel.getStyleClass().add(message.isUser() ? "user-username" : "other-username");

        // Création du conteneur pour le message
        Label messageLabel = new Label(message.getContent());
        messageLabel.getStyleClass().add("message-label");

        HBox messageContainer = new HBox();
        messageContainer.getStyleClass().add(message.isUser() ? "user-message" : "other-message");
        messageContainer.getChildren().add(messageLabel);

        contentBox.getChildren().addAll(usernameLabel, messageContainer);
        messageGroup.getChildren().add(contentBox);
    }
}