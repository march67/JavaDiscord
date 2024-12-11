// src/main/java/com/javadiscordproject/controllers/component/ConversationListController.java

package com.javadiscordproject.controllers.component;

import com.javadiscordproject.managers.ControllerManager;
import com.javadiscordproject.models.UserDTO;
import com.javadiscordproject.services.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import java.net.URL;
import java.util.List;

public class ConversationListController {
    @FXML
    private TextField searchField;
    @FXML
    private VBox conversationListContainer;

    private final UserService userService;

    public ConversationListController() {
        this.userService = new UserService();
    }

    @FXML
    public void initialize() {
        loadStyles();
        setupSearch();
    }

    private void loadStyles() {
        try {
            URL cssUrl = getClass().getResource("/styles/component/conversation-list-component.css");
            if (cssUrl != null) {
                conversationListContainer.getStylesheets().add(cssUrl.toExternalForm());
            }
        } catch (Exception e) {
            System.err.println("CSS error: " + e.getMessage());
        }
    }

    private void setupSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            clearResults();
            if (newValue.length() >= 2) {
                List<UserDTO> users = userService.getUsers(newValue);
                displayResults(users);
            }
        });
    }

    private void clearResults() {
        ObservableList<Node> children = conversationListContainer.getChildren();
        if (children.size() > 1) {
            children.subList(1, children.size()).clear();
        }
    }

    private void displayResults(List<UserDTO> users) {
        clearResults();
        for (UserDTO user : users) {
            Label label = new Label(user.username());
            label.setOnMouseClicked(event -> {
                ConversationController conversationController =
                        (ConversationController) ControllerManager.getInstance()
                                .getController("conversation");

                if (conversationController != null) {
                    conversationController.changeConversation(user.username());
                }
            });
            conversationListContainer.getChildren().add(label);
        }
    }
}