package com.javadiscordproject.controllers.component;

import com.javadiscordproject.controllers.view.AuthController;
import com.javadiscordproject.models.UserDTO;
import com.javadiscordproject.services.api.AuthService;
import com.javadiscordproject.services.api.IAuthService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegisterController {
    private static final Logger logger = Logger.getLogger(RegisterController.class.getName());

    @FXML private VBox formContainer;
    @FXML private TextField registerUsername;
    @FXML private TextField registerEmail;
    @FXML private PasswordField registerPassword;
    @FXML private PasswordField registerConfirmPassword;
    @FXML private Button registerButton;
    @FXML private Button showLogin;

    private AuthController authController;
    private final IAuthService authService;

    public RegisterController() {
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

    public void setAuthController(AuthController controller) {
        this.authController = controller;
    }

    private void setupEventHandlers() {
        registerButton.setOnAction(event -> handleRegistration());
        showLogin.setOnAction(event -> authController.showLoginForm());
    }

    private void handleRegistration() {
        if (isLoading()) {
            return;
        }

        String username = registerUsername.getText().trim();
        String email = registerEmail.getText().trim();
        String password = registerPassword.getText();
        String confirmPassword = registerConfirmPassword.getText();

        if (validateInput(username, email, password, confirmPassword)) {
            setLoading(true);

            UserDTO user = new UserDTO(email, username, password);

            authService.register(user)
                    .thenAccept(success -> {
                        Platform.runLater(() -> {
                            setLoading(false);
                            if (success) {
                                handleSuccessfulRegistration();
                            } else {
                                showError("Registration Failed",
                                        "Please verify your information and try again.");
                            }
                        });
                    })
                    .exceptionally(error -> {
                        Platform.runLater(() -> {
                            setLoading(false);
                            showError("Error",
                                    "An error occurred during registration: " +
                                            error.getMessage());
                        });
                        return null;
                    });
        }
    }

    private boolean validateInput(String username, String email,
                                  String password, String confirmPassword) {
        if (username == null || username.trim().isEmpty()) {
            showError("Validation Error", "Username is required");
            return false;
        }

        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showError("Validation Error", "Invalid email address");
            return false;
        }

        if (password == null || password.length() < 6) {
            showError("Validation Error", "Password must be at least 6 characters long");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            showError("Validation Error", "Passwords do not match");
            return false;
        }

        return true;
    }

    private void handleSuccessfulRegistration() {
        showSuccess("Registration Successful", "Your account has been created successfully!");
        clearForm();
        authController.onSuccessfulAuth();
    }

    private void showError(String title, String content) {
        showAlert(Alert.AlertType.ERROR, title, content);
    }

    private void showSuccess(String title, String content) {
        showAlert(Alert.AlertType.INFORMATION, title, content);
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void setLoading(boolean loading) {
        registerButton.setDisable(loading);
        showLogin.setDisable(loading);
        registerUsername.setDisable(loading);
        registerEmail.setDisable(loading);
        registerPassword.setDisable(loading);
        registerConfirmPassword.setDisable(loading);
    }

    private boolean isLoading() {
        return registerButton.isDisabled();
    }

    public void clearForm() {
        registerUsername.clear();
        registerEmail.clear();
        registerPassword.clear();
        registerConfirmPassword.clear();
    }
}