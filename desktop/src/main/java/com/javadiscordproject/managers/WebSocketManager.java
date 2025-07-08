package com.javadiscordproject.managers;

import com.javadiscordproject.models.UserDTO;
import com.javadiscordproject.services.websocket.WebSocketInitializer;

public class WebSocketManager {

    private WebSocketInitializer webSocketInitializer;

    private static WebSocketManager INSTANCE;

    private WebSocketManager() {
    }

    public static WebSocketManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new WebSocketManager();
        }
        return INSTANCE;
    }

    /**
     * Initialize WebSocket connection in a separate thread
     */
    public void initWebSocketConnectionAsync() {
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
                } else {
                    System.out.println("Reconnection attempt failed");
                }
            });
            retryThread.setDaemon(true);
            retryThread.start();
        }
    }

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
}
