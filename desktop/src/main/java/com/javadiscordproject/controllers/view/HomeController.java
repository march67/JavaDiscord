package com.javadiscordproject.controllers.view;

import com.javadiscordproject.controllers.component.BaseController;
import com.javadiscordproject.controllers.component.LeftNavbarController;
import com.javadiscordproject.controllers.component.ConversationListController;
import com.javadiscordproject.controllers.component.ConversationController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HomeController extends BaseController {
    private static final Logger logger = Logger.getLogger(HomeController.class.getName());

    @FXML
    private AnchorPane rootPane;
    @FXML
    private HBox container;

    private LeftNavbarController navbarController;
    private VBox navbarView;
    private ConversationListController conversationListController;
    private VBox conversationListView;
    private ConversationController conversationController;
    private VBox conversationView;

    @FXML
    public void initialize() {
        try {
            URL cssUrl = getClass().getResource("/styles/view/home-view.css");
            if (cssUrl != null) {
                rootPane.getStylesheets().add(cssUrl.toExternalForm());
            } else {
                logger.warning("CSS file not found: /styles/view/home-view.css");
            }
        } catch (NullPointerException e) {
            logger.log(Level.SEVERE, "Failed to load CSS", e);
        }
        loadComponents();
    }

    private void loadComponents() {
        try {

            FXMLLoader navbarLoader = new FXMLLoader(getClass().getResource("/fxml/component/left-navbar-component.fxml"));
            navbarView = navbarLoader.load();
            navbarController = navbarLoader.getController();

            FXMLLoader conversationListLoader = new FXMLLoader(getClass().getResource("/fxml/component/conversation-list-component.fxml"));
            conversationListView = conversationListLoader.load();
            conversationListController = conversationListLoader.getController();

            FXMLLoader conversationLoader = new FXMLLoader(getClass().getResource("/fxml/component/conversation-component.fxml"));
            conversationView = conversationLoader.load();
            conversationController = conversationLoader.getController();

            container.getChildren().addAll(navbarView, conversationListView, conversationView);

            HBox.setHgrow(conversationView, javafx.scene.layout.Priority.ALWAYS);

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load FXML components", e);
        }
    }

    private void handleLogout() {
        sceneManager.showLoginScene();
    }
}