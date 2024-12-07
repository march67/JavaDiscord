package com.javadiscordproject.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class AuthController extends BaseController {
    @FXML
    private Button connecterButton;

    @FXML
    public void initialize() {
        connecterButton.setOnAction(event -> {
            // Navigate to home scene when connect button is clicked
            sceneManager.showHomeScene();
        });
    }
}