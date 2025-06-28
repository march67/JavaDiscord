package com.javadiscordproject.managers;

import com.javadiscordproject.services.SceneFactory;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static com.javadiscordproject.utils.UIConstants.DEFAULT_HEIGHT;
import static com.javadiscordproject.utils.UIConstants.DEFAULT_WIDTH;

public class SceneManager {
    private static SceneManager instance;
    private final Stage primaryStage;

    private SceneManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Discord2");
        primaryStage.setMinWidth(DEFAULT_WIDTH);
        primaryStage.setMinHeight(DEFAULT_HEIGHT);
    }

    public static void initialize(Stage primaryStage) {
        if (instance == null) {
            instance = new SceneManager(primaryStage);
        }
    }

    public static SceneManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("SceneManager must be initialized with a Stage before use!");
        }
        return instance;
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