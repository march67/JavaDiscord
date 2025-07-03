package com.javadiscordproject.controllers.component;

import com.javadiscordproject.controllers.view.AuthViewController;
import com.javadiscordproject.models.RegisterDTO;
import com.javadiscordproject.services.api.auth.AuthService;
import com.javadiscordproject.services.api.auth.IAuthService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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

    private AuthViewController authController;
    private final IAuthService authService;

    public RegisterController() {
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

    public void setAuthController(AuthViewController controller) {
        this.authController = controller;
    }

    private void setupEventHandlers() {
        registerButton.setOnAction(event -> handleRegistration());
        showLogin.setOnAction(event -> authController.showLoginForm());
    }
    private void setupEnterKeyListeners() {
        addEnterKeyListenerToAllInputs(formContainer);
    }

    private void addEnterKeyListenerToAllInputs(Node node) {
        if (node instanceof TextInputControl) {
            node.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    handleRegistration();
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

            RegisterDTO registerDto = new RegisterDTO(username, password, email);

            authService.register(registerDto)
                    .thenAccept(success -> {
                        Platform.runLater(() -> {
                            setLoading(false);
                            if (success) {
                                handleSuccessfulRegistration(username);
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

    private void handleSuccessfulRegistration(String username) {
        showSuccess("Registration Successful",
                "Your account has been created successfully! Please login with your credentials.");
        clearForm();
        authController.showLoginAfterRegistration();
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