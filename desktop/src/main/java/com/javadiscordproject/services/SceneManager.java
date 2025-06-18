package com.javadiscordproject.services;

import javafx.scene.Scene;
import javafx.stage.Stage;

import static com.javadiscordproject.utils.UIConstants.DEFAULT_HEIGHT;
import static com.javadiscordproject.utils.UIConstants.DEFAULT_WIDTH;

public class SceneManager {
    // TODO merge avec scene factory
    private final Stage primaryStage;

    public SceneManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Mon Application");

        primaryStage.setMinWidth(DEFAULT_WIDTH);
        primaryStage.setMinHeight(DEFAULT_HEIGHT);
    }

    public void showLoginScene() {
        Scene loginScene = SceneFactory.createScene(
                SceneFactory.SceneType.AUTH,
                this
        );

        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    public void showHomeScene() {
        Scene homeScene = SceneFactory.createScene(
                SceneFactory.SceneType.HOME,
                this
        );

        primaryStage.setScene(homeScene);
    }
}