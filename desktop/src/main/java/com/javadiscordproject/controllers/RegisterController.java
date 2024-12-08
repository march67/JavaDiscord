package com.javadiscordproject.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {

    @FXML private TextField registerUsername;
    @FXML private PasswordField registerPassword;
    @FXML private PasswordField registerConfirmPassword;
    @FXML private Button registerButton;
    @FXML private Button showLogin;

    private AuthController authController;

    @FXML
    public void initialize() {
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