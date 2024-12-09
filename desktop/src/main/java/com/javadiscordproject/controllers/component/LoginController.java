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

public class LoginController {
    private static final Logger logger = Logger.getLogger(LoginController.class.getName());

    @FXML private VBox formContainer;
    @FXML private TextField loginUsername;
    @FXML private PasswordField loginPassword;
    @FXML private Button loginButton;
    @FXML private Button showRegister;

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
        loginButton.setOnAction(event -> handleLogin());
        showRegister.setOnAction(event -> authController.showRegisterForm());
    }

    private void handleLogin() {
        String username = loginUsername.getText().trim();
        String password = loginPassword.getText();

        if (validateInput(username, password)) {
            authController.onSuccessfulAuth();
        }
    }

    private boolean validateInput(String username, String password) {
        // TODO: Implement real validation logic when backend is ready
        return true;
    }

    public void clearForm() {
        loginUsername.clear();
        loginPassword.clear();
    }
}