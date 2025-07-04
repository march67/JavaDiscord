package com.javadiscordproject.controllers.component;

import com.javadiscordproject.managers.SessionManager;
import com.javadiscordproject.models.FriendNotificationDTO;
import com.javadiscordproject.services.IFriendRequestAcceptedListener;
import com.javadiscordproject.services.api.friend.FriendRequestService;
import com.javadiscordproject.services.api.friend.IFriendRequestService;
import com.javadiscordproject.services.websocket.WebSocketService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
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
    private IFriendRequestService friendRequestService;
    private Stage notificationPopup;
    private VBox requestsContainer;
    private WebSocketService webSocketService;

    private IFriendRequestAcceptedListener friendRequestAcceptedListener;

    @FXML
    public void initialize() {
        loadStyles();
        friendRequestService = new FriendRequestService();
        webSocketService = new WebSocketService();

        notificationDot.setRadius(6);
        notificationDot.setMouseTransparent(true);
        StackPane.setAlignment(notificationDot, javafx.geometry.Pos.TOP_RIGHT);

        initializeWebSocketConnection();

        checkForPendingRequests();
    }

    private void initializeWebSocketConnection() {
        try {
            boolean connected = webSocketService.connectToWebSocket();
            if (connected) {
                LOGGER.info("WebSocket connecté avec succès");

                webSocketService.subscribeToNotifications();

                webSocketService.setNotificationReceivedCallback(this::onNotificationReceived);

                LOGGER.info("Abonnement aux notifications WebSocket réussi");
            } else {
                LOGGER.warning("Échec de la connexion WebSocket");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'initialisation WebSocket", e);
        }
    }

    private void onNotificationReceived(FriendNotificationDTO notification) {
        String currentUsername = SessionManager.getInstance().getUsername();
        boolean isForCurrentUser = currentUsername != null &&
                currentUsername.equals(notification.getUsername_receiver());

        if (isForCurrentUser) {
            Platform.runLater(() -> {
                setHasNotifications(true);
                if (notificationPopup != null && notificationPopup.isShowing()) {
                    refreshRequestsList();
                }
            });
        }
    }

    public void setFriendRequestAcceptedListener(IFriendRequestAcceptedListener listener) {
        this.friendRequestAcceptedListener = listener;
    }

    private void loadStyles() {
        try {
            URL cssUrl = getClass().getResource(CSS_PATH);
            if (cssUrl != null) {
                rootContainer.getStylesheets().add(cssUrl.toExternalForm());
                rootContainer.getStyleClass().add("notification-container");
                notificationButton.getStyleClass().add("notification-button");
                notificationDot.getStyleClass().add("notification-dot");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load CSS", e);
        }
    }

    private void checkForPendingRequests() {
        friendRequestService.getPendingFriendRequests().thenAccept(requests -> {
            Platform.runLater(() -> setHasNotifications(!requests.isEmpty()));
        });
    }

    @FXML
    private void onNotificationClick() {
        if (notificationPopup != null && notificationPopup.isShowing()) {
            notificationPopup.close();
            notificationPopup = null;
        } else {
            openNotificationPopup();
        }
    }

    private void openNotificationPopup() {
        notificationPopup = new Stage();
        notificationPopup.initModality(Modality.APPLICATION_MODAL);
        notificationPopup.setTitle("Notifications");

        VBox mainContent = new VBox(10);
        mainContent.getStyleClass().add("notification-popup");

        Label titleLabel = new Label("Demandes d'amis en attente");
        titleLabel.getStyleClass().add("notification-popup-title");
        mainContent.getChildren().add(titleLabel);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.getStyleClass().add("notification-popup-scrollpane");
        scrollPane.setFitToWidth(true);

        requestsContainer = new VBox(10);
        requestsContainer.getStyleClass().add("notification-popup-requests-container");

        refreshRequestsList();

        scrollPane.setContent(requestsContainer);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        mainContent.getChildren().add(scrollPane);

        Scene scene = new Scene(mainContent, 400, 500);

        URL mainCssUrl = getClass().getResource("/styles/styles.css");
        if (mainCssUrl != null) {
            scene.getStylesheets().add(mainCssUrl.toExternalForm());
        }

        URL notificationCssUrl = getClass().getResource(CSS_PATH);
        if (notificationCssUrl != null) {
            scene.getStylesheets().add(notificationCssUrl.toExternalForm());
        }

        notificationPopup.setScene(scene);
        notificationPopup.setOnCloseRequest(e -> {
            notificationPopup = null;
            requestsContainer = null;
        });
        notificationPopup.show();
    }

    private void refreshRequestsList() {
        if (requestsContainer == null) return;

        friendRequestService.getPendingFriendRequests().thenAccept(requests -> {
            Platform.runLater(() -> {
                requestsContainer.getChildren().clear();

                if (requests.isEmpty()) {
                    Label noRequestsLabel = new Label("Aucune demande d'ami en attente");
                    noRequestsLabel.getStyleClass().add("notification-popup-no-requests");
                    requestsContainer.getChildren().add(noRequestsLabel);
                    setHasNotifications(false);
                } else {
                    for (String username : requests) {
                        HBox requestItem = createFriendRequestItem(username);
                        requestsContainer.getChildren().add(requestItem);
                    }
                }
            });
        });
    }

    private HBox createFriendRequestItem(String username) {
        HBox container = new HBox(10);
        container.setAlignment(Pos.CENTER_LEFT);
        container.getStyleClass().add("friend-request-item");

        Label usernameLabel = new Label(username);
        usernameLabel.getStyleClass().add("friend-request-username");
        usernameLabel.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(usernameLabel, Priority.ALWAYS);

        HBox buttonsContainer = new HBox(5);
        buttonsContainer.setAlignment(Pos.CENTER_RIGHT);

        Button acceptButton = new Button("✓");
        acceptButton.getStyleClass().add("friend-request-accept-button");
        acceptButton.setOnAction(e -> handleAcceptRequest(username, container));

        Button declineButton = new Button("✗");
        declineButton.getStyleClass().add("friend-request-decline-button");
        declineButton.setOnAction(e -> handleDeclineRequest(username, container));

        buttonsContainer.getChildren().addAll(acceptButton, declineButton);
        container.getChildren().addAll(usernameLabel, buttonsContainer);
        return container;
    }

    private void handleAcceptRequest(String username, HBox requestItem) {
        requestItem.setDisable(true);
        requestItem.setOpacity(0.5);

        friendRequestService.acceptFriendRequest(username).thenAccept(success -> {
            if (success) {
                Platform.runLater(() -> {
                    refreshRequestsList();
                    checkForPendingRequests();

                    if (friendRequestAcceptedListener != null) {
                        friendRequestAcceptedListener.onFriendRequestAccepted(username);
                    }
                });
            } else {
                Platform.runLater(() -> {
                    requestItem.setDisable(false);
                    requestItem.setOpacity(1.0);
                });
            }
        }).exceptionally(ex -> {
            LOGGER.log(Level.SEVERE, "Error accepting friend request", ex);
            Platform.runLater(() -> {
                requestItem.setDisable(false);
                requestItem.setOpacity(1.0);
            });
            return null;
        });
    }

    private void handleDeclineRequest(String username, HBox requestItem) {
        requestItem.setDisable(true);
        requestItem.setOpacity(0.5);

        friendRequestService.declineFriendRequest(username).thenAccept(success -> {
            if (success) {
                Platform.runLater(() -> {
                    refreshRequestsList();
                    checkForPendingRequests();
                });
            } else {
                Platform.runLater(() -> {
                    requestItem.setDisable(false);
                    requestItem.setOpacity(1.0);
                });
            }
        }).exceptionally(ex -> {
            LOGGER.log(Level.SEVERE, "Error declining friend request", ex);
            Platform.runLater(() -> {
                requestItem.setDisable(false);
                requestItem.setOpacity(1.0);
            });
            return null;
        });
    }

    public void setHasNotifications(boolean hasNotifications) {
        this.hasNotifications = hasNotifications;
        notificationDot.setVisible(hasNotifications);
    }

    public void cleanup() {
        if (webSocketService != null) {
            webSocketService.disconnect();
        }
    }
}