package com.javadiscordproject.services;

import com.javadiscordproject.controllers.AuthController;
import com.javadiscordproject.controllers.BaseController;
import com.javadiscordproject.controllers.HomeController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

public class SceneFactory {
    private static final double DEFAULT_WIDTH = 800;
    private static final double DEFAULT_HEIGHT = 600;
    private static final String CSS_PATH = "/styles/styles.css";

    private static final Map<SceneType, String> SCENE_RESOURCES = Map.of(
            SceneType.LOGIN, "/fxml/view/auth-view.fxml",
            SceneType.HOME, "/fxml/view/home-view.fxml"
    );

    private static final Map<SceneType, Class<?>> SCENE_CONTROLLERS = Map.of(
            SceneType.LOGIN, AuthController.class,
            SceneType.HOME, HomeController.class
    );

    public static Scene createScene(SceneType type, SceneManager sceneManager) {
        try {
            String fxmlPath = SCENE_RESOURCES.get(type);
            Class<?> controllerClass = SCENE_CONTROLLERS.get(type);

            if (fxmlPath == null || controllerClass == null) {
                throw new IllegalArgumentException("Unsupported scene type: " + type);
            }

            URL fxmlUrl = SceneFactory.class.getResource(fxmlPath);
            if (fxmlUrl == null) {
                throw new RuntimeException("FXML resource not found: " + fxmlPath);
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            Object controller = loader.getController();
            if (controller instanceof BaseController) {
                ((BaseController) controller).setSceneManager(sceneManager);
            }

            Scene scene = new Scene(root, DEFAULT_WIDTH, DEFAULT_HEIGHT);

            URL cssUrl = SceneFactory.class.getResource(CSS_PATH);
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            } else {
                System.err.println("Warning: CSS resource not found: " + CSS_PATH);
            }

            return scene;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load scene: " + type, e);
        }
    }

    public enum SceneType {
        LOGIN,
        HOME
    }
}