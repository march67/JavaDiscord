package com.javadiscordproject.controllers.component;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.net.URL;
import java.util.logging.Logger;
import java.util.logging.Level;

public class NotificationButtonController {
    private static final Logger LOGGER = Logger.getLogger(NotificationButtonController.class.getName());
    private static final String CSS_PATH = "/styles/component/notification-button-component.css";

    @FXML
    private StackPane rootContainer;

    @FXML
    private Button notificationButton;

    @FXML
    private Circle notificationDot;

    private boolean hasNotifications = false;

    @FXML
    public void initialize() {
        loadStyles();

        notificationDot.setRadius(6);

        notificationDot.setMouseTransparent(true);

        StackPane.setAlignment(notificationDot, javafx.geometry.Pos.TOP_RIGHT);

        setHasNotifications(true);
    }

    private void loadStyles() {
        try {
            URL cssUrl = getClass().getResource(CSS_PATH);
            if (cssUrl != null) {
                rootContainer.getStylesheets().add(cssUrl.toExternalForm());
                // Appliquer les classes CSS
                rootContainer.getStyleClass().add("notification-container");
                notificationButton.getStyleClass().add("notification-button");
                notificationDot.getStyleClass().add("notification-dot");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load CSS", e);
        }
    }

    @FXML
    private void onNotificationClick() {
        openNotificationPopup();
        setHasNotifications(false);
    }

    private void openNotificationPopup() {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Notifications");

        VBox content = new VBox(10);
        content.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Label label = new Label("Vos notifications apparaîtront ici");
        content.getChildren().add(label);

        Scene scene = new Scene(content, 300, 400);
        popup.setScene(scene);
        popup.show();
    }

    public void setHasNotifications(boolean hasNotifications) {
        this.hasNotifications = hasNotifications;
        notificationDot.setVisible(hasNotifications);
    }
}