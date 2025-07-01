package com.javadiscordproject.controllers.component;
import com.javadiscordproject.managers.SceneManager;
import com.javadiscordproject.managers.SessionManager;
import com.javadiscordproject.models.FriendDTO;
import com.javadiscordproject.services.api.friend.IFriendService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.geometry.Pos;

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

    private List<FriendDTO> allFriends = new ArrayList<>();
    private FriendSelectionListener friendSelectionListener;
    private IFriendService friendService;
    public void setFriendService(IFriendService friendService) {
        this.friendService = friendService;
    }

    public interface FriendSelectionListener {
        void onFriendSelected(String username);
    }

    @FXML
    public void initialize() {
        loadStyles();
        setupToggleButtons();
        setupSearchField();
        setupLogoutButton();
        displayCurrentUser();
        showLoadingMessage();
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
        displayFriends(allFriends);
    }

    private void showAddFriendView() {
        searchField.setVisible(false);
        searchField.setManaged(false);
        friendsList.getChildren().clear();

        VBox addFriendView = new VBox(10);
        addFriendView.getStyleClass().add("add-friend-view");

        Label titleLabel = new Label("Ajouter un ami");
        titleLabel.getStyleClass().add("add-friend-title");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Nom d'utilisateur");
        usernameField.getStyleClass().add("add-friend-field");

        Label statusLabel = new Label();
        statusLabel.getStyleClass().add("status-label");
        statusLabel.setVisible(false);

        Button addButton = new Button("Envoyer la demande");
        addButton.getStyleClass().add("add-friend-button");
        addButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            if (!username.isEmpty() && friendService != null) {
                addButton.setDisable(true);
                statusLabel.setText("Envoi en cours...");
                statusLabel.setVisible(true);

                friendService.addFriend(username).thenAccept(success -> {
                    Platform.runLater(() -> {
                        if (success) {
                            statusLabel.setText("Demande d'ami envoyée avec succès!");
                            statusLabel.getStyleClass().add("success");
                            usernameField.clear();

                            new Thread(() -> {
                                try {
                                    Thread.sleep(2000);
                                    Platform.runLater(() -> {
                                        friendsButton.setSelected(true);
                                        if (friendService != null) {
                                            friendService.getFriendsList().thenAccept(friends -> {
                                                updateFriendsList(friends);
                                            });
                                        }
                                    });
                                } catch (InterruptedException ex) {
                                    // Ignorer
                                }
                            }).start();
                        } else {
                            statusLabel.setText("Erreur lors de l'envoi de la demande");
                            statusLabel.getStyleClass().add("error");
                        }
                        addButton.setDisable(false);
                    });
                }).exceptionally(throwable -> {
                    Platform.runLater(() -> {
                        statusLabel.setText("Erreur de connexion");
                        statusLabel.getStyleClass().add("error");
                        addButton.setDisable(false);
                        LOGGER.log(Level.SEVERE, "Erreur lors de l'ajout d'ami", throwable);
                    });
                    return null;
                });
            }
        });

        addFriendView.getChildren().addAll(titleLabel, usernameField, statusLabel, addButton);
        friendsList.getChildren().add(addFriendView);
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
                friendSelectionListener.onFriendSelected(friend.getUsername());
            }
            friendsList.getChildren().forEach(node -> node.getStyleClass().remove("selected"));
            friendItem.getStyleClass().add("selected");
        });

        return friendItem;
    }

    public void setFriendSelectionListener(FriendSelectionListener listener) {
        this.friendSelectionListener = listener;
    }
}