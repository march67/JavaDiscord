package com.javadiscordproject.main;

import com.javadiscordproject.services.SceneManager;
import com.javadiscordproject.services.websocket.WebSocketInitializer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class MainApplication extends Application {

    private WebSocketInitializer webSocketInitializer;

    @Override
    public void start(Stage primaryStage) {
        // First, set up the main application UI
        SceneManager sceneManager = new SceneManager(primaryStage);
        sceneManager.showLoginScene();

        // Then initialize WebSocket connection in a separate thread to avoid blocking UI
        initWebSocketConnectionAsync();
    }

    /**
     * Initialize WebSocket connection in a separate thread
     */
    private void initWebSocketConnectionAsync() {
        // Create a new thread to avoid blocking the JavaFX application thread
        Thread wsThread = new Thread(() -> {
            try {
                System.out.println("Setting up WebSocket connection...");
                webSocketInitializer = new WebSocketInitializer();

                // Initialize connection and send message
                boolean success = webSocketInitializer.initializeAndSendMessage();

                if (success) {
                    System.out.println("WebSocket initialized and message sent successfully");
                } else {
                    System.out.println("WebSocket initialization failed or message sending failed");
                    System.out.println("Application will continue without WebSocket functionality");
                }
            } catch (Exception e) {
                System.out.println("Error during WebSocket initialization: " + e.getMessage());
                e.printStackTrace();
                System.out.println("Application will continue without WebSocket functionality");
            }
        });

        // Set as daemon thread so it doesn't prevent application exit
        wsThread.setDaemon(true);
        wsThread.setName("WebSocket-Init-Thread");
        wsThread.start();
    }

    /**
     * Try to reconnect to WebSocket server
     * Can be called from UI when user requests reconnection
     */
    public void retryWebSocketConnection() {
        if (webSocketInitializer != null) {
            Thread retryThread = new Thread(() -> {
                boolean success = webSocketInitializer.retryConnection();
                if (success) {
                    System.out.println("Successfully reconnected to WebSocket server");
                    webSocketInitializer.sendStaticMessage();
                } else {
                    System.out.println("Reconnection attempt failed");
                }
            });
            retryThread.setDaemon(true);
            retryThread.start();
        }
    }

    @Override
    public void stop() {
        // Clean up resources when the application is closing
        if (webSocketInitializer != null) {
            try {
                webSocketInitializer.shutdown();
            } catch (Exception e) {
                System.out.println("Error during WebSocket shutdown: " + e.getMessage());
            }
        }
        System.out.println("Application shutting down");
    }

    public static void main(String[] args) {
        launch(args);
    }
}