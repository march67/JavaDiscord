package com.javadiscordproject.controllers.component;

import com.javadiscordproject.services.api.friend.IFriendService;
import com.javadiscordproject.services.api.friend.FriendService.AddFriendResult;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddFriendController {
    private static final Logger LOGGER = Logger.getLogger(AddFriendController.class.getName());

    @FXML
    private TextField usernameField;
    @FXML
    private Label statusLabel;
    @FXML
    private Button addButton;

    private IFriendService friendService;
    private Runnable onSuccessCallback;

    public void setFriendService(IFriendService friendService) {
        this.friendService = friendService;
    }

    public void setOnSuccessCallback(Runnable callback) {
        this.onSuccessCallback = callback;
    }

    @FXML
    public void initialize() {
        statusLabel.setVisible(false);
    }

    public void resetForm() {
        usernameField.clear();
        statusLabel.setVisible(false);
        statusLabel.getStyleClass().removeAll("success", "error");
        addButton.setDisable(false);
    }

    @FXML
    private void handleAddFriend() {
        String username = usernameField.getText().trim();
        if (!username.isEmpty() && friendService != null) {
            addButton.setDisable(true);
            statusLabel.setText("Envoi en cours...");
            statusLabel.setVisible(true);

            statusLabel.getStyleClass().removeAll("success", "error");

            friendService.addFriend(username).thenAccept(result -> {
                javafx.application.Platform.runLater(() -> {
                    if (result.isSuccess()) {
                        statusLabel.setText(result.getMessage());
                        statusLabel.getStyleClass().add("success");
                        usernameField.clear();

                        new Thread(() -> {
                            try {
                                Thread.sleep(1000);
                                javafx.application.Platform.runLater(() -> {
                                    addButton.setDisable(false);
                                });

                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }

                            javafx.application.Platform.runLater(() -> {
                                resetForm();

                                if (onSuccessCallback != null) {
                                    onSuccessCallback.run();
                                }
                            });
                        }).start();
                    } else {
                        statusLabel.setText(result.getMessage());
                        statusLabel.getStyleClass().add("error");
                        addButton.setDisable(false);
                    }
                });
            }).exceptionally(throwable -> {
                javafx.application.Platform.runLater(() -> {
                    statusLabel.setText("Erreur de connexion");
                    statusLabel.getStyleClass().add("error");
                    addButton.setDisable(false);
                    LOGGER.log(Level.SEVERE, "Erreur lors de l'ajout d'ami", throwable);
                });
                return null;
            });
        }
    }
}