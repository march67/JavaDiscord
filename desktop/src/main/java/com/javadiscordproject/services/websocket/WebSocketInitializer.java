package com.javadiscordproject.services.websocket;

public class WebSocketInitializer {

    private WebSocketService webSocketService;
    private boolean connectionAttempted = false;

    public WebSocketInitializer() {
        this.webSocketService = WebSocketService.getInstance();
    }

    public boolean initializeAndSendMessage() {
        connectionAttempted = true;

        try {
            System.out.println("Initializing WebSocket connection...");

            boolean connected = webSocketService.connectToWebSocket();

            if (connected) {
                System.out.println("Static message sent successfully: ");
                return true;
            } else {
                System.out.println("Failed to connect to WebSocket server - continue with application startup");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error initializing WebSocket: " + e.getMessage());
            e.printStackTrace();
            System.out.println("Continuing application startup despite WebSocket error");
            return false;
        }
    }

    public boolean retryConnection() {
        if (webSocketService.isConnected()) {
            return true; // Already connected
        }

        try {
            System.out.println("Retrying WebSocket connection...");
            return webSocketService.connectToWebSocket();
        } catch (Exception e) {
            System.out.println("Error retrying WebSocket connection: " + e.getMessage());
            return false;
        }
    }
    public void shutdown() {
        if (webSocketService != null) {
            try {
                webSocketService.disconnect();
            } catch (Exception e) {
                System.out.println("Error during WebSocket shutdown: " + e.getMessage());
            }
        }
    }
}