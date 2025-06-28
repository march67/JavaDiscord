package com.javadiscordproject.controllers.view;

import com.javadiscordproject.controllers.base.BaseViewController;
import com.javadiscordproject.controllers.component.LeftNavbarController;
import com.javadiscordproject.controllers.component.FriendListController;
import com.javadiscordproject.controllers.component.ConversationController;
import com.javadiscordproject.managers.SessionManager;
import com.javadiscordproject.models.FriendDTO;
import com.javadiscordproject.services.api.friend.FriendService;
import com.javadiscordproject.services.api.friend.IFriendService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HomeViewController extends BaseViewController {
    private static final Logger logger = Logger.getLogger(HomeViewController.class.getName());

    @FXML
    private AnchorPane rootPane;
    @FXML
    private HBox container;

    private LeftNavbarController navbarController;
    private VBox navbarView;
    private FriendListController friendListController;
    private VBox friendListView;
    private ConversationController conversationController;
    private VBox conversationView;

    private final IFriendService friendService = new FriendService();

    @FXML
    public void initialize() {
        loadCss();
        loadComponents();
        setupEventHandlers();
        loadFriendsListAsync();
    }

    private void loadCss() {
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
    }

    private void loadComponents() {
        try {
            FXMLLoader navbarLoader = new FXMLLoader(getClass().getResource("/fxml/component/left-navbar-component.fxml"));
            navbarView = navbarLoader.load();
            navbarController = navbarLoader.getController();

            FXMLLoader friendListLoader = new FXMLLoader(getClass().getResource("/fxml/component/friend-list-component.fxml"));
            friendListView = friendListLoader.load();
            friendListController = friendListLoader.getController();

            FXMLLoader conversationLoader = new FXMLLoader(getClass().getResource("/fxml/component/conversation-component.fxml"));
            conversationView = conversationLoader.load();
            conversationController = conversationLoader.getController();

            container.getChildren().addAll(navbarView, friendListView, conversationView);

            HBox.setHgrow(conversationView, javafx.scene.layout.Priority.ALWAYS);

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load FXML components", e);
        }
    }

    private void setupEventHandlers() {
        if (friendListController != null) {
            friendListController.setFriendSelectionListener(username -> {
                if (conversationController != null) {
                    conversationController.changeConversation(username);
                }
            });
        }

        setupLogoutHandler();
    }

    private void loadFriendsListAsync() {
        CompletableFuture<List<FriendDTO>> friendsFuture = friendService.getFriendsList();

        friendsFuture.thenAccept(friends -> {
            logger.info("Friends loaded successfully: " + friends.size() + " friends");

            Platform.runLater(() -> {
                if (friendListController != null) {
                    friendListController.updateFriendsList(friends);
                }
            });
        }).exceptionally(throwable -> {
            logger.log(Level.SEVERE, "Failed to load friends list", throwable);

            Platform.runLater(() -> {
            });
            return null;
        });
    }

    private void setupLogoutHandler() {
    }

    public void logout() {
        SessionManager.getInstance().clearSession();
        sceneManager.showLoginScene();
    }
}