package com.javadiscordproject.controllers.component;
import com.javadiscordproject.listeners.FriendSelectionListener;
import com.javadiscordproject.managers.SceneManager;
import com.javadiscordproject.managers.SessionManager;
import com.javadiscordproject.models.FriendDTO;
import com.javadiscordproject.services.api.friend.IFriendService;
import com.javadiscordproject.services.websocket.WebSocketService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.geometry.Pos;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class FriendListController {
    private static final Logger LOGGER = Logger.getLogger(FriendListController.class.getName());
    private static final String CSS_PATH = "/styles/component/friend-list-component.css";

    @FXML private VBox rootContainer;
    @FXML private ToggleButton friendsButton;
    @FXML private ToggleButton addFriendButton;
    @FXML private TextField searchField;
    @FXML private ScrollPane friendsScrollPane;
    @FXML private VBox friendsList;
    @FXML private ToggleGroup navigationGroup;
    @FXML private Button logoutButton;
    @FXML private Label currentUserLabel;
    @FXML private Button refreshButton;

    private List<FriendDTO> allFriends = new ArrayList<>();
    private FriendSelectionListener friendSelectionListener;
    private IFriendService friendService;
    private WebSocketService webSocketService;
    private VBox addFriendView;
    private AddFriendController addFriendController;

    public void setFriendService(IFriendService friendService) {
        this.friendService = friendService;
        loadAddFriendView();
    }

    public void setWebSocketService(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
        if (addFriendController != null) {
            addFriendController.setWebSocketService(webSocketService);
        }
        setupWebSocketSubscription();
    }

    private void setupWebSocketSubscription() {
        if (webSocketService != null && webSocketService.isConnected()) {
            webSocketService.setFriendNotificationReceivedCallback(username -> {
                String currentUser = SessionManager.getInstance().getUsername();
                System.out.println("=== FRIEND NOTIFICATION RECEIVED ===");
                System.out.println("Received username: " + username);
                System.out.println("Current user: " + currentUser);

                if (username.equals(currentUser)) {
                    System.out.println("Refreshing friends list...");
                    refreshFriendsList();
                }

                System.out.println("====================================");
            });

            webSocketService.subscribeToFriendNotifications();
        }
    }

    @FXML
    public void initialize() {
        loadStyles();
        setupToggleButtons();
        setupSearchField();
        setupLogoutButton();
        displayCurrentUser();
        showLoadingMessage();
        loadAddFriendView();
        refreshButton.setOnAction(e -> refreshFriendsList());
    }


    private void loadAddFriendView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/component/add-friend-component.fxml"));
            addFriendView = loader.load();
            addFriendController = loader.getController();
            addFriendController.setFriendService(friendService);
            if (webSocketService != null) {
                addFriendController.setWebSocketService(webSocketService);
            }
            addFriendController.setOnSuccessCallback(() -> {
                navigateToFriendsList();
                refreshFriendsList();
            });
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load add friend view", e);
        }
    }

    private void loadStyles() {
        try {
            URL cssUrl = getClass().getResource(CSS_PATH);
            if (cssUrl != null) {
                rootContainer.getStylesheets().add(cssUrl.toExternalForm());
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load CSS", e);
        }
    }

    private void setupToggleButtons() {
        navigationGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == null) {
                oldToggle.setSelected(true);
            }
        });

        friendsButton.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                showFriendsList();
            }
        });

        addFriendButton.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                showAddFriendView();
            }
        });
    }

    private void setupSearchField() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterFriends(newValue);
        });
    }

    private void setupLogoutButton() {
        logoutButton.setOnAction(e -> {
            handleLogout();
        });
    }

    private void displayCurrentUser() {
        String username = SessionManager.getInstance().getUsername();
        if (username != null) {
            currentUserLabel.setText(username);
        }
    }

    private void handleLogout() {
        SessionManager.getInstance().clearSession();

        SceneManager sceneManager = SceneManager.getInstance();
        if (sceneManager != null) {
            sceneManager.showLoginScene();
        }
    }

    private void showLoadingMessage() {
        Platform.runLater(() -> {
            friendsList.getChildren().clear();
            Label loadingLabel = new Label("Chargement des amis...");
            loadingLabel.getStyleClass().add("loading-label");
            friendsList.getChildren().add(loadingLabel);
        });
    }

    private void showFriendsList() {
        searchField.setVisible(true);
        searchField.setManaged(true);
        refreshButton.setVisible(true);
        refreshButton.setManaged(true);
        displayFriends(allFriends);
    }

    private void showAddFriendView() {
        searchField.setVisible(false);
        searchField.setManaged(false);
        refreshButton.setVisible(false);
        refreshButton.setManaged(false);
        friendsList.getChildren().clear();

        if (addFriendView != null) {
            if (addFriendController != null) {
                addFriendController.resetForm();
            }
            friendsList.getChildren().add(addFriendView);
        } else {
            Label errorLabel = new Label("Erreur de chargement");
            errorLabel.getStyleClass().add("error-label");
            friendsList.getChildren().add(errorLabel);
        }
    }

    public void updateFriendsList(List<FriendDTO> friends) {
        Platform.runLater(() -> {
            this.allFriends = new ArrayList<>(friends);
            if (friendsButton.isSelected()) {
                displayFriends(friends);
            }
        });
    }

    public void refreshFriendsList() {
        if (friendService != null) {
            showLoadingMessage();

            friendService.getFriendsList().thenAccept(friends -> {
                Platform.runLater(() -> {
                    updateFriendsList(friends);
                    if (friendsButton.isSelected()) {
                        displayFriends(friends);
                    }

                    LOGGER.info("Friends list refreshed: " + friends.size() + " friends");
                });
            }).exceptionally(throwable -> {
                Platform.runLater(() -> {
                    friendsList.getChildren().clear();
                    Label errorLabel = new Label("Erreur lors du chargement des amis");
                    errorLabel.getStyleClass().add("error-label");
                    friendsList.getChildren().add(errorLabel);
                });
                LOGGER.log(Level.SEVERE, "Failed to refresh friends list", throwable);
                return null;
            });
        }
    }

    private void filterFriends(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            displayFriends(allFriends);
        } else {
            String lowerCaseFilter = searchText.toLowerCase();
            List<FriendDTO> filteredFriends = allFriends.stream()
                    .filter(friend -> friend.getUsername().toLowerCase().contains(lowerCaseFilter))
                    .collect(Collectors.toList());
            displayFriends(filteredFriends);
        }
    }

    private void displayFriends(List<FriendDTO> friends) {
        Platform.runLater(() -> {
            friendsList.getChildren().clear();

            if (friends.isEmpty()) {
                Label emptyLabel = new Label("Aucun ami trouvé");
                emptyLabel.getStyleClass().add("empty-label");
                friendsList.getChildren().add(emptyLabel);
                return;
            }

            for (FriendDTO friend : friends) {
                HBox friendItem = createFriendItem(friend);
                friendsList.getChildren().add(friendItem);
            }
        });
    }

    private HBox createFriendItem(FriendDTO friend) {
        HBox friendItem = new HBox(10);
        friendItem.getStyleClass().add("friend-item");
        friendItem.setAlignment(Pos.CENTER_LEFT);

        Label avatar = new Label(friend.getUsername().substring(0, 1).toUpperCase());
        avatar.getStyleClass().add("friend-avatar");

        Label nameLabel = new Label(friend.getUsername());
        nameLabel.getStyleClass().add("friend-name");
        HBox.setHgrow(nameLabel, Priority.ALWAYS);

        friendItem.getChildren().addAll(avatar, nameLabel);

        friendItem.setOnMouseClicked(e -> {
            if (friendSelectionListener != null) {
                friendSelectionListener.onFriendSelected(friend.getUsername(), friend.getConversationId());
            }
            friendsList.getChildren().forEach(node -> node.getStyleClass().remove("selected"));
            friendItem.getStyleClass().add("selected");
        });

        return friendItem;
    }

    public void setFriendSelectionListener(FriendSelectionListener listener) {
        this.friendSelectionListener = listener;
    }

    public void navigateToFriendsList() {
        friendsButton.setSelected(true);
        showFriendsList();
    }
}