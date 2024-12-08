package com.javadiscordproject.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import java.io.IOException;
import javafx.scene.layout.AnchorPane;

public class AuthController extends BaseController {
    @FXML
    private StackPane stackContainer;
    @FXML
    private AnchorPane rootPane;

    private LoginController loginController;
    private RegisterController registerController;
    private VBox loginView;
    private VBox registerView;

    @FXML
    public void initialize() {
        String cssPath = getClass().getResource("/styles/auth-view.css").toExternalForm();
        rootPane.getStylesheets().add(cssPath);
        rootPane.getStyleClass().add("auth-container");

        loadComponents();
        showLoginForm();
    }

    private void loadComponents() {
        try {
            FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/fxml/componenent/login-component.fxml"));
            loginView = loginLoader.load();
            loginController = loginLoader.getController();
            loginController.setAuthController(this);

            FXMLLoader registerLoader = new FXMLLoader(getClass().getResource("/fxml/componenent/register-component.fxml"));
            registerView = registerLoader.load();
            registerController = registerLoader.getController();
            registerController.setAuthController(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showLoginForm() {
        stackContainer.getChildren().clear();
        stackContainer.getChildren().add(loginView);
        loginController.clearForm();
    }

    public void showRegisterForm() {
        stackContainer.getChildren().clear();
        stackContainer.getChildren().add(registerView);
        registerController.clearForm();
    }

    public void onSuccessfulAuth() {
        sceneManager.showHomeScene();
    }
}