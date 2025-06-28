package com.javadiscordproject.controllers.component;

import com.javadiscordproject.managers.SessionManager;
import com.javadiscordproject.models.Message;
import com.javadiscordproject.services.AutoResponseService;
import com.javadiscordproject.services.MessageDisplayService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
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
    private static final String CURRENT_USER_DISPLAY = "Moi";

    @FXML private VBox rootContainer;
    @FXML private ScrollPane messageScrollPane;
    @FXML private VBox messageList;
    @FXML private TextField inputField;
    @FXML private Label recipientName;

    private final MessageDisplayService messageDisplayService;
    private final AutoResponseService autoResponseService;
    private String currentUser;
    private String loggedInUsername;
    private boolean hasActiveConversation = false;

    public ConversationController() {
        this.messageDisplayService = new MessageDisplayService();
        this.autoResponseService = new AutoResponseService();
        this.currentUser = null;
        this.loggedInUsername = SessionManager.getInstance().getUsername();
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
        String displayName = loggedInUsername != null ? loggedInUsername : CURRENT_USER_DISPLAY;
        Message userMessage = new Message(messageText, displayName, true);
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
        hasActiveConversation = true;
        recipientName.setText(username);
        messageList.getChildren().clear();

        // Activer le champ de saisie
        inputField.setDisable(false);
        inputField.requestFocus();

        String loggedUser = loggedInUsername != null ? loggedInUsername : CURRENT_USER_DISPLAY;
        Message systemMessage = new Message(
                String.format("Début de la conversation entre %s et %s", loggedUser, username),
                "Système",
                false
        );
        messageDisplayService.displayMessage(systemMessage, messageList, messageScrollPane);
    }

    private void displayMessage(Message message) {
        Platform.runLater(() ->
                messageDisplayService.displayMessage(message, messageList, messageScrollPane)
        );
    }

    public void updateLoggedInUsername() {
        this.loggedInUsername = SessionManager.getInstance().getUsername();
    }
}