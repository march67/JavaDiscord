package com.javadiscordproject.services;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {
    private final Stage primaryStage;

    public SceneManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Mon Application");
    }

    public void showLoginScene() {
        Scene loginScene = SceneFactory.createScene(
                SceneFactory.SceneType.LOGIN,
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