package com.javadiscordproject.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class HomeController extends BaseController {
    @FXML
    private Button disconnectButton;

    @FXML
    public void initialize() {
        setupEventHandlers();
    }

    private void setupEventHandlers() {
        disconnectButton.setOnAction(event -> handleLogout());
    }
    private void ClearSession() {
    }
    private void handleLogout() {
        ClearSession();
        sceneManager.showLoginScene();
    }
}