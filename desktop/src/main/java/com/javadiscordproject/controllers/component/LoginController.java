package com.javadiscordproject.controllers.component;

import com.javadiscordproject.controllers.view.AuthViewController;
import com.javadiscordproject.managers.SessionManager;
import com.javadiscordproject.models.LoginDTO;
import com.javadiscordproject.services.api.auth.AuthService;
import com.javadiscordproject.services.api.auth.IAuthService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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

    public void setAuthController(AuthViewController controller) {
        this.authController = controller;
    }

    private void setupEventHandlers() {
        loginButton.setOnAction(event -> handleLogin());
        showRegister.setOnAction(event -> authController.showRegisterForm());
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
                    .thenAccept(success -> {
                        Platform.runLater(() -> {
                            setLoading(false);
                            if (success) {
                                handleSuccessfulLogin(username);
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

    private void handleSuccessfulLogin(String username) {
        SessionManager.getInstance().createSession(username, "email@example.com");
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