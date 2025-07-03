package com.javadiscordproject.controllers.component;

import com.javadiscordproject.controllers.view.AuthViewController;
import com.javadiscordproject.managers.SessionManager;
import com.javadiscordproject.managers.WebSocketManager;
import com.javadiscordproject.models.LoginDTO;
import com.javadiscordproject.services.api.auth.AuthService;
import com.javadiscordproject.services.api.auth.IAuthService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginController {
    private static final Logger logger = Logger.getLogger(LoginController.class.getName());

    @FXML private VBox formContainer;
    @FXML private TextField loginUsername;
    @FXML private PasswordField loginPassword;
    @FXML private Button loginButton;
    @FXML private Button showRegister;

    private AuthViewController authController;
    private final IAuthService authService;

    public LoginController() {
        this.authService = new AuthService();
    }

    @FXML
    public void initialize() {
        loadCSS();
        setupEventHandlers();
        setupEnterKeyListeners();
    }

    private void loadCSS() {
        try {
            URL cssUrl = getClass().getResource("/styles/component/auth-component.css");
            if (cssUrl != null) {
                formContainer.getStylesheets().add(cssUrl.toExternalForm());
            } else {
                logger.warning("CSS file not found: /styles/component/auth-component.css");
            }
        } catch (NullPointerException e) {
            logger.log(Level.SEVERE, "Failed to load CSS", e);
        }
    }
    private void bootWebSocket(String username){

        WebSocketManager instance = WebSocketManager.getInstance();
        instance.initWebSocketConnectionAsync();
    }
    public void setAuthController(AuthViewController controller) {
        this.authController = controller;
    }

    private void setupEventHandlers() {
        loginButton.setOnAction(event -> handleLogin());
        showRegister.setOnAction(event -> authController.showRegisterForm());
    }

    private void setupEnterKeyListeners() {
        addEnterKeyListenerToAllInputs(formContainer);
    }

    private void addEnterKeyListenerToAllInputs(Node node) {
        if (node instanceof TextInputControl) {
            node.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    handleLogin();
                    event.consume();
                }
            });
        }

        if (node instanceof javafx.scene.Parent) {
            javafx.scene.Parent parent = (javafx.scene.Parent) node;
            for (Node child : parent.getChildrenUnmodifiable()) {
                addEnterKeyListenerToAllInputs(child);
            }
        }
    }

    private void handleLogin() {
        if (isLoading()) {
            return;
        }

        String username = loginUsername.getText().trim();
        String password = loginPassword.getText();

        if (validateInput(username, password)) {
            setLoading(true);

            LoginDTO loginDto = new LoginDTO(username, password);

            authService.login(loginDto)
                    .thenAccept(token -> {
                        Platform.runLater(() -> {
                            setLoading(false);
                            if (token != null) {
                                handleSuccessfulLogin(username, token);
                            } else {
                                showError("Login Failed",
                                        "Invalid username or password.");
                            }
                        });
                    })
                    .exceptionally(error -> {
                        Platform.runLater(() -> {
                            setLoading(false);
                            showError("Error",
                                    "An error occurred during login: " +
                                            error.getMessage());
                        });
                        return null;
                    });
        }
    }

    private boolean validateInput(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            showError("Validation Error", "Username is required");
            return false;
        }

        if (password == null || password.isEmpty()) {
            showError("Validation Error", "Password is required");
            return false;
        }

        return true;
    }

    private void handleSuccessfulLogin(String username, String token) {
        SessionManager.getInstance().createSession(username, token);
        bootWebSocket(username);
        clearForm();
        authController.onSuccessfulAuth();
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void setLoading(boolean loading) {
        loginButton.setDisable(loading);
        showRegister.setDisable(loading);
        loginUsername.setDisable(loading);
        loginPassword.setDisable(loading);
    }

    private boolean isLoading() {
        return loginButton.isDisabled();
    }

    public void clearForm() {
        loginUsername.clear();
        loginPassword.clear();
    }
}