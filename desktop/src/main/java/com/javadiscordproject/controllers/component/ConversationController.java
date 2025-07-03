package com.javadiscordproject.controllers.component;

import com.javadiscordproject.managers.SessionManager;
import com.javadiscordproject.managers.WebSocketManager;
import com.javadiscordproject.models.MessageDTO;
import com.javadiscordproject.services.MessageDisplayService;
import com.javadiscordproject.services.api.message.MessageService;
import com.javadiscordproject.services.websocket.WebSocketService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.input.KeyCode;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConversationController {
    private static final Logger LOGGER = Logger.getLogger(ConversationController.class.getName());
    private static final String CSS_PATH = "/styles/component/conversation-component.css";
    private static final String CURRENT_USER_DISPLAY = "Moi";

    @FXML private VBox rootContainer;
    @FXML private ScrollPane messageScrollPane;
    @FXML private VBox messageList;
    @FXML private TextField inputField;
    @FXML private Label recipientName;

    private final MessageDisplayService messageDisplayService;
    private final MessageService messageService;
    private String currentUser;
    private String loggedInUsername;
    private Long currentConversationId;
    private boolean hasActiveConversation = false;

    private final WebSocketService webSocketService;
    public ConversationController() {
        this.webSocketService = new WebSocketService();
        this.messageDisplayService = new MessageDisplayService();
        this.messageService = new MessageService();
        this.currentUser = null;
        this.loggedInUsername = SessionManager.getInstance().getUsername();
        this.currentConversationId = null;
    }

    @FXML
    public void initialize() {
        loadStyles();
        setupMessageInput();
        showWelcomeMessage();
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
            if (event.getCode() == KeyCode.ENTER && !inputField.getText().trim().isEmpty() && hasActiveConversation) {
                sendMessage();
            }
        });

        inputField.setDisable(true);
    }

    private void showWelcomeMessage() {
        Platform.runLater(() -> {
            messageList.getChildren().clear();
            recipientName.setText("Bienvenue");

            VBox welcomeContainer = new VBox(10);
            welcomeContainer.setAlignment(Pos.CENTER);
            welcomeContainer.setStyle("-fx-padding: 50;");

            Label welcomeLabel = new Label("Bienvenue dans votre messagerie");
            welcomeLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333;");

            Label instructionLabel = new Label("Sélectionnez un ami dans la liste de gauche pour commencer une conversation");
            instructionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666; -fx-wrap-text: true;");
            instructionLabel.setWrapText(true);
            instructionLabel.setMaxWidth(300);

            welcomeContainer.getChildren().addAll(welcomeLabel, instructionLabel);
            messageList.getChildren().add(welcomeContainer);

            VBox.setVgrow(welcomeContainer, javafx.scene.layout.Priority.ALWAYS);
        });
    }

    private void sendMessage() {
        if (!hasActiveConversation || currentUser == null) {
            return;
        }

        String messageText = inputField.getText().trim();
        if (messageText.isEmpty()) return;

        String displayName = loggedInUsername != null ? loggedInUsername : CURRENT_USER_DISPLAY;
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        // Envoyer via WebSocket
        boolean sent = webSocketService.sendMessage(displayName, messageText);

        if (sent) {
            MessageDTO userMessage = new MessageDTO(displayName, messageText, currentTime);
            messageDisplayService.displayMessage(userMessage, messageList, messageScrollPane, true);
            inputField.clear();
        } else {
            System.err.println("Échec de l'envoi du message");
        }
    }

    public void changeConversation(String username, Long conversationId) {
        Platform.runLater(() -> {
            System.out.println("Conversation ID reçu : " + conversationId);
            LOGGER.info("Conversation ID reçu : " + conversationId);

            this.currentConversationId = conversationId;
            initializeConversation(username);
            loadConversationMessages();
        });
    }

    private void initializeConversation(String username) {
        currentUser = username;
        hasActiveConversation = true;
        recipientName.setText(username);
        messageList.getChildren().clear();

        inputField.setDisable(false);
        inputField.requestFocus();
    }

    private void loadConversationMessages() {
        if (currentConversationId == null) {
            return;
        }

        new Thread(() -> {
            try {
                List<MessageDTO> messages = messageService.getConversationMessages(currentConversationId);
                Platform.runLater(() -> {
                    for (MessageDTO message : messages) {
                        boolean isCurrentUser = message.getUsername().equals(loggedInUsername);
                        messageDisplayService.displayMessage(message, messageList, messageScrollPane, isCurrentUser);
                    }
                });
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Failed to load conversation messages", e);
            }
        }).start();
    }

    public void updateLoggedInUsername() {
        this.loggedInUsername = SessionManager.getInstance().getUsername();
    }
}