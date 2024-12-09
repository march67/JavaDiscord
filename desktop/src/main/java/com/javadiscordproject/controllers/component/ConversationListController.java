package com.javadiscordproject.controllers.component;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import java.net.URL;

public class ConversationListController {
    @FXML
    private TextField searchField;
    @FXML
    private VBox conversationListContainer;

    @FXML
    public void initialize() {
        try {
            URL cssUrl = getClass().getResource("/styles/component/conversation-list-component.css");
            if (cssUrl != null) {
                String cssPath = cssUrl.toExternalForm();
                conversationListContainer.getStylesheets().add(cssPath);
            } else {
                System.err.println("CSS file not found");
            }
        } catch (NullPointerException e) {
            System.err.println("Failed to load CSS: " + e.getMessage());
        }
    }
}