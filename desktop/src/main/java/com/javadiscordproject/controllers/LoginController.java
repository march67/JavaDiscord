package com.javadiscordproject.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private TextField loginUsername;
    @FXML private PasswordField loginPassword;
    @FXML private Button loginButton;
    @FXML private Button showRegister;

    private AuthController authController;

    @FXML
    public void initialize() {
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