package com.javadiscordproject.controllers.component;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LeftNavbarController {
    private static final Logger logger = Logger.getLogger(LeftNavbarController.class.getName());

    @FXML
    private VBox navbarContainer;

    private NotificationButtonController notificationController;

    @FXML
    public void initialize() {
        loadCss();
        loadNotificationButton();
    }

    private void loadCss() {
        try {
            URL cssUrl = getClass().getResource("/styles/component/left-navbar-component.css");
            if (cssUrl != null) {
                String cssPath = cssUrl.toExternalForm();
                navbarContainer.getStylesheets().add(cssPath);
            } else {
                System.err.println("CSS file not found");
            }
        } catch (NullPointerException e) {
            System.err.println("Failed to load CSS: " + e.getMessage());
        }
    }

    private void loadNotificationButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/component/notification-button-component.fxml"));
            StackPane notificationButton = loader.load();
            notificationController = loader.getController();

            navbarContainer.getChildren().add(0, notificationButton);

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load notification button", e);
        }
    }

    public NotificationButtonController getNotificationController() {
        return notificationController;
    }
}