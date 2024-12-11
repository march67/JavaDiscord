package com.javadiscordproject.controllers.component;

import com.javadiscordproject.controllers.view.AuthController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegisterController {
    private static final Logger logger = Logger.getLogger(RegisterController.class.getName());

    @FXML private VBox formContainer;
    @FXML private TextField registerUsername;
    @FXML private PasswordField registerPassword;
    @FXML private PasswordField registerConfirmPassword;
    @FXML private Button registerButton;
    @FXML private Button showLogin;

    private AuthController authController;

    @FXML
    public void initialize() {
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
        setupEventHandlers();
    }

    public void setAuthController(AuthController controller) {
        this.authController = controller;
    }

    private void setupEventHandlers() {
        registerButton.setOnAction(event -> handleRegistration());
        showLogin.setOnAction(event -> authController.showLoginForm());
    }

    private void handleRegistration() {
        String username = registerUsername.getText().trim();
        String password = registerPassword.getText();
        String confirmPassword = registerConfirmPassword.getText();

        if (validateInput(username, password, confirmPassword)) {
            authController.onSuccessfulAuth();
        }
    }

    private boolean validateInput(String username, String password, String confirmPassword) {
        // TODO: Implement real validation logic when backend is ready
        return true;
    }

    public void clearForm() {
        registerUsername.clear();
        registerPassword.clear();
        registerConfirmPassword.clear();
    }
}