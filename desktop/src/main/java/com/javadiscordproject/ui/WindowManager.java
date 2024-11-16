// WindowManager.java

package com.javadiscordproject.ui;

import com.javadiscordproject.services.ConfigManager;
import com.javadiscordproject.ui.pages.LoginPage;
import com.javadiscordproject.utils.Constants;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The WindowManager class is responsible for initializing and configuring the primary application window.
 * It sets the dimensions, style, and initial page (login page) for the main stage.
 */
public class WindowManager {

    /**
     * Initializes the primary stage of the application by setting its dimensions, scene, and styles.
     *
     * @param primaryStage The main stage passed by JavaFX, representing the primary window of the application.
     */
    public void init(Stage primaryStage) {

        int windowWidth = ConfigManager.getIntProperty("window.width", Constants.DEFAULT_WINDOW_WIDTH);
        int windowHeight = ConfigManager.getIntProperty("window.height", Constants.DEFAULT_WINDOW_HEIGHT);

        LoginPage loginPage = new LoginPage();
        Scene scene = new Scene(loginPage.getRoot(), windowWidth, windowHeight);

        var cssResource = getClass().getResource("/styles/application.css");
        if (cssResource != null) {
            scene.getStylesheets().add(cssResource.toExternalForm());
        } else {
            System.err.println("CSS file not found. Please check the path.");
        }

        primaryStage.setScene(scene);
    }
}
