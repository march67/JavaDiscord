package com.javadiscordproject.controllers.component;

import com.javadiscordproject.managers.ControllerManager;
import com.javadiscordproject.models.Message;
import com.javadiscordproject.services.AutoResponseService;
import com.javadiscordproject.services.MessageDisplayService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.input.KeyCode;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.net.URL;

public class ConversationController {
    private static final Logger LOGGER = Logger.getLogger(ConversationController.class.getName());
    private static final String CSS_PATH = "/styles/component/conversation-component.css";
    private static final String DEFAULT_USER = "User1";
    private static final String CURRENT_USER_DISPLAY = "Moi";

    @FXML private VBox rootContainer;
    @FXML private ScrollPane messageScrollPane;
    @FXML private VBox messageList;
    @FXML private TextField inputField;
    @FXML private Label recipientName;

    private final MessageDisplayService messageDisplayService;
    private final AutoResponseService autoResponseService;
    private String currentUser;

    public ConversationController() {
        this.messageDisplayService = new MessageDisplayService();
        this.autoResponseService = new AutoResponseService();
        this.currentUser = DEFAULT_USER;
    }

    @FXML
    public void initialize() {
        loadStyles();
        setupMessageInput();
        initializeConversation(DEFAULT_USER);
        ControllerManager.getInstance().registerController("conversation", this);
    }

    private void loadStyles() {
        try {
            URL cssUrl = getClass().getResource(CSS_PATH);
            if (cssUrl != null) {
                rootContainer.getStylesheets().add(cssUrl.toExternalForm());
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load CSS", e);
        }
    }

    private void setupMessageInput() {
        inputField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER && !inputField.getText().trim().isEmpty()) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        String messageText = inputField.getText().trim();
        Message userMessage = new Message(messageText, CURRENT_USER_DISPLAY, true);
        messageDisplayService.displayMessage(userMessage, messageList, messageScrollPane);
        inputField.clear();
        autoResponseService.simulateResponse(currentUser, this::displayMessage);
    }

    public void changeConversation(String username) {
        Platform.runLater(() -> {
            initializeConversation(username);
        });
    }

    private void initializeConversation(String username) {
        currentUser = username;
        recipientName.setText(username);
        messageList.getChildren().clear();
        Message systemMessage = new Message("Début de la conversation avec " + username, "Système", false);
        messageDisplayService.displayMessage(systemMessage, messageList, messageScrollPane);
    }

    private void displayMessage(Message message) {
        Platform.runLater(() ->
                messageDisplayService.displayMessage(message, messageList, messageScrollPane)
        );
    }
}