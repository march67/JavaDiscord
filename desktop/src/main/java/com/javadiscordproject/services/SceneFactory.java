package com.javadiscordproject.services;

import com.javadiscordproject.controllers.AuthController;
import com.javadiscordproject.controllers.BaseController;
import com.javadiscordproject.controllers.MainViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Map;

public class SceneFactory {
    private static final double DEFAULT_WIDTH = 800;
    private static final double DEFAULT_HEIGHT = 600;

    // Mapping des types de scènes aux ressources FXML
    private static final Map<SceneType, String> SCENE_RESOURCES = Map.of(
            SceneType.LOGIN, "/fxml/auth-view.fxml",
            SceneType.HOME, "/fxml/main-view.fxml"
    );

    // Mapping des types de scènes aux contrôleurs
    private static final Map<SceneType, Class<?>> SCENE_CONTROLLERS = Map.of(
            SceneType.LOGIN, AuthController.class,
            SceneType.HOME, MainViewController.class
    );

    public static Scene createScene(SceneType type, Stage primaryStage, SceneManager sceneManager) {
        try {
            // Récupérer le chemin FXML et le type de contrôleur
            String fxmlPath = SCENE_RESOURCES.get(type);
            Class<?> controllerClass = SCENE_CONTROLLERS.get(type);

            if (fxmlPath == null || controllerClass == null) {
                throw new IllegalArgumentException("Type de scène non supporté : " + type);
            }

            // Charger le FXML
            FXMLLoader loader = new FXMLLoader(SceneFactory.class.getResource(fxmlPath));
            Parent root = loader.load();

            // Récupérer et configurer le contrôleur
            Object controller = loader.getController();
            if (controller instanceof BaseController) {
                ((BaseController) controller).setSceneManager(sceneManager);
            }

            // Créer la scène
            Scene scene = new Scene(root, DEFAULT_WIDTH, DEFAULT_HEIGHT);

            // Ajouter le CSS
            scene.getStylesheets().add(SceneFactory.class.getResource("/styles/styles.css").toExternalForm());

            return scene;
        } catch (IOException e) {
            throw new RuntimeException("Erreur de chargement de la scène", e);
        }
    }

    public enum SceneType {
        LOGIN,
        HOME
    }
}