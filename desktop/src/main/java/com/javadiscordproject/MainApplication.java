package com.javadiscordproject;

import com.javadiscordproject.managers.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApplication extends Application {
    @Override
    public void start(Stage primaryStage) {
        SceneManager.initialize(primaryStage);
        SceneManager.getInstance().showLoginScene();
        SceneManager.getInstance().showHomeScene();
    }

    public static void main(String[] args) {
        launch(args);
    }
}