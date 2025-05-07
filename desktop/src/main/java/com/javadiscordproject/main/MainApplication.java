package com.javadiscordproject.main;

import com.javadiscordproject.services.SceneManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class MainApplication extends Application {


    @Override
    public void start(Stage primaryStage) {
        // First, set up the main application UI
        SceneManager sceneManager = new SceneManager(primaryStage);
        sceneManager.showLoginScene();
    }

    public static void main(String[] args) {
        launch(args);
    }
}