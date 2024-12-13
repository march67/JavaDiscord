package com.javadiscordproject.controllers.component;

import com.javadiscordproject.managers.ControllerManager;
import com.javadiscordproject.managers.SessionManager;
import com.javadiscordproject.models.UserDTO;
import com.javadiscordproject.services.UserService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import java.net.URL;
import java.util.List;

public class ConversationListController {
    private static final String CSS_PATH = "/styles/component/conversation-list-component.css";
    private static final int MIN_SEARCH_LENGTH = 1;

    @FXML private TextField searchField;
    @FXML private VBox conversationListContainer;
    @FXML private Label currentUserLabel;
    @FXML private Button logoutButton;

    private final UserService userService;
    private Runnable onLogoutRequested;
    private Popup searchPopup;
    private VBox searchResultsBox;

    public ConversationListController() {
        this.userService = new UserService();
    }

    public void setOnLogoutRequested(Runnable callback) {
        this.onLogoutRequested = callback;
    }

    @FXML
    public void initialize() {
        loadStyles();
        initializeSearchPopup();
        setupSearch();
        setupLogoutButton();
        updateUserLabel();
    }

    private void loadStyles() {
        try {
            URL cssUrl = getClass().getResource(CSS_PATH);
            if (cssUrl != null) {
                String stylesheet = cssUrl.toExternalForm();
                conversationListContainer.getStylesheets().add(stylesheet);
            }
        } catch (Exception e) {
            System.err.println("CSS error: " + e.getMessage());
        }
    }

    private void initializeSearchPopup() {
        searchPopup = new Popup();
        searchResultsBox = new VBox();
        searchResultsBox.getStyleClass().addAll("search-results-box", "popup-container");
        searchResultsBox.getStylesheets().add(getClass().getResource(CSS_PATH).toExternalForm());
        searchPopup.getContent().add(searchResultsBox);

        setupSearchPopupBehavior();
    }

    private void setupSearchPopupBehavior() {
        searchField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                Platform.runLater(() -> {
                    if (!searchField.isFocused()) {
                        searchPopup.hide();
                    }
                });
            }
        });
    }

    private void setupSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() >= MIN_SEARCH_LENGTH) {
                List<UserDTO> users = userService.getUsers(newValue);
                showSearchResults(users);
            } else {
                searchPopup.hide();
            }
        });
    }

    private void showSearchResults(List<UserDTO> users) {
        searchResultsBox.getChildren().clear();

        if (users.isEmpty()) {
            addNoResultsLabel();
        } else {
            users.forEach(user -> searchResultsBox.getChildren().add(createUserLabel(user)));
        }

        showPopupBelowSearchField();
    }

    private void addNoResultsLabel() {
        Label noResults = new Label("Aucun résultat");
        noResults.getStyleClass().add("no-results-label");
        searchResultsBox.getChildren().add(noResults);
    }

    private void showPopupBelowSearchField() {
        if (!searchPopup.isShowing()) {
            Bounds bounds = searchField.localToScreen(searchField.getBoundsInLocal());
            searchPopup.show(searchField, bounds.getMinX(), bounds.getMaxY());
        }
    }

    private Label createUserLabel(UserDTO user) {
        Label label = new Label(user.getUsername());
        label.getStyleClass().add("search-result-item");
        label.setOnMouseClicked(event -> handleUserSelection(user.getUsername()));
        return label;
    }

    private void handleUserSelection(String username) {
        ConversationController conversationController =
                (ConversationController) ControllerManager.getInstance()
                        .getController("conversation");
        if (conversationController != null) {
            conversationController.changeConversation(username);
            searchPopup.hide();
            searchField.clear();
        }
    }

    private void setupLogoutButton() {
        logoutButton.setOnAction(event -> {
            if (onLogoutRequested != null) {
                onLogoutRequested.run();
            }
        });
    }

    private void updateUserLabel() {
        String username = SessionManager.getInstance().getUsername();
        if (username != null) {
            currentUserLabel.setText(username);
        }
    }
}