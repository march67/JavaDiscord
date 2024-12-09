package com.javadiscordproject.controllers.view;

import com.javadiscordproject.controllers.component.BaseController;
import com.javadiscordproject.controllers.component.LoginController;
import com.javadiscordproject.controllers.component.RegisterController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import java.io.IOException;
import javafx.scene.layout.AnchorPane;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AuthController extends BaseController {
    private static final Logger logger = Logger.getLogger(AuthController.class.getName());

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
        try {
            URL cssUrl = getClass().getResource("/styles/view/auth-view.css");
            if (cssUrl != null) {
                String cssPath = cssUrl.toExternalForm();
                rootPane.getStylesheets().add(cssPath);
                rootPane.getStyleClass().add("auth-container");
            } else {
                logger.warning("CSS file not found: /styles/view/auth-view.css");
            }
        } catch (NullPointerException e) {
            logger.log(Level.SEVERE, "Failed to load CSS", e);
        }
        loadComponents();
        showLoginForm();
    }

    private void loadComponents() {
        try {
            FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/fxml/component/login-component.fxml"));
            loginView = loginLoader.load();
            loginController = loginLoader.getController();
            loginController.setAuthController(this);

            FXMLLoader registerLoader = new FXMLLoader(getClass().getResource("/fxml/component/register-component.fxml"));
            registerView = registerLoader.load();
            registerController = registerLoader.getController();
            registerController.setAuthController(this);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load FXML components", e);
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