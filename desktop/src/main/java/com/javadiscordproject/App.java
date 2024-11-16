package com.javadiscordproject;

import com.javadiscordproject.ui.WindowManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
        WindowManager windowManager = new WindowManager();
        windowManager.init(primaryStage);
        primaryStage.setTitle("Java Discord Project");
        primaryStage.show();
    }
}