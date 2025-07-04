package com.javadiscordproject.services.websocket;

/**
 * Class responsible for initializing the WebSocket connection
 * and sending the initial test message.
 */
public class WebSocketInitializer {

    private WebSocketService webSocketService;
    private boolean connectionAttempted = false;

    /**
     * Default constructor
     */
    public WebSocketInitializer() {
        webSocketService = new WebSocketService();
    }

    /**
     * Initialize the WebSocket connection and send the static test message
     * This method won't throw exceptions to prevent application failure if WebSocket is unavailable
     * @return true if connection was successful and message was sent
     */
    public boolean initializeAndSendMessage() {
        connectionAttempted = true;

        try {
            System.out.println("Initializing WebSocket connection...");

            // Connect to WebSocket server with timeout
            boolean connected = webSocketService.connectToWebSocket();

            if (connected) {
                // Send the static test message
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

    /**
     * Retry connection if the first attempt failed
     * @return true if connection was successful
     */
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

    /**
     * Get the WebSocketService instance
     * @return the WebSocketService instance
     */
    public WebSocketService getWebSocketService() {
        return webSocketService;
    }

    /**
     * Check if a connection attempt was made
     * @return true if connection was attempted
     */
    public boolean wasConnectionAttempted() {
        return connectionAttempted;
    }

    /**
     * Close the WebSocket connection
     */
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