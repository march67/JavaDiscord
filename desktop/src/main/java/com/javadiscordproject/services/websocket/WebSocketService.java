package com.javadiscordproject.services.websocket;

import com.javadiscordproject.managers.SessionManager;
import com.javadiscordproject.models.ChatMessageDTO;
import com.javadiscordproject.models.NotificationDTO;
import com.javadiscordproject.utils.CustomStompSessionHandler;
import javafx.application.Platform;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.javadiscordproject.utils.WsConstants.*;

public class WebSocketService {

    private StompSession stompSession;
    private boolean isConnected = false;
    private static final int CONNECTION_TIMEOUT_SECONDS = 3; // Timeout after 3 seconds

    public WebSocketService() {}

    // Connect to WebSocket server with timeout
    public boolean connectToWebSocket() {
        System.out.println("Attempting to connect to " + WEBSOCKET_URL);

        try {
            // Create WebSocket client
            StandardWebSocketClient webSocketClient = new StandardWebSocketClient();
            WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);

            // Add Jackson converter for JSON conversion
            stompClient.setMessageConverter(new MappingJackson2MessageConverter());

            // Connect and get session with timeout
            StompSessionHandler sessionHandler = new CustomStompSessionHandler();

            // Add JWT token to HTTP headers instead of STOMP headers
            WebSocketHttpHeaders httpHeaders = new WebSocketHttpHeaders();
            String jwtToken = SessionManager.getInstance().getToken();
            System.out.println("JWT token: " + jwtToken);
            httpHeaders.add("Authorization", "Bearer " + jwtToken);

            // Use empty STOMP headers since authentication is now in HTTP headers
            StompHeaders connectHeaders = new StompHeaders();
            connectHeaders.add("Authorization", "Bearer " + jwtToken);

            stompSession = stompClient
                    .connectAsync(WEBSOCKET_URL, httpHeaders, connectHeaders, sessionHandler)
                    .get(CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS);

            if (stompSession != null && stompSession.isConnected()) {
                stompSession.subscribe(TOPIC_MESSAGES, new StompFrameHandler() {
                    @Override
                    public Type getPayloadType(StompHeaders headers) {
                        return ChatMessageDTO.class;
                    }

                    @Override
                    public void handleFrame(StompHeaders headers, Object payload) {
                        ChatMessageDTO receivedMsg = (ChatMessageDTO) payload;
                        Platform.runLater(() -> {
                            System.out.println("Received: " + receivedMsg.getSender() + ": " +
                                    receivedMsg.getContent() + " (" + receivedMsg.getTimestamp() + ")");
                        });
                    }
                });

                isConnected = true;
                System.out.println("Successfully connected to WebSocket server");
                return true;
            } else {
                System.out.println("Failed to establish connection");
                isConnected = false;
                return false;
            }

        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error connecting: " + e.getMessage());
            e.printStackTrace();
            isConnected = false;
            return false;
        } catch (TimeoutException e) {
            System.out.println("Connection timed out after " + CONNECTION_TIMEOUT_SECONDS + " seconds");
            isConnected = false;
            return false;
        }
    }

    // Send a message to the WebSocket server
    public boolean sendMessage(String sender, String content) {
        if (stompSession != null && stompSession.isConnected()) {
            try {
                // Create message with current timestamp
                String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
                ChatMessageDTO message = new ChatMessageDTO(sender, content, timestamp);

                // Send message
                stompSession.send(APP_ENDPOINT, message);
                System.out.println("Sent message: " + content);
                return true;

            } catch (Exception e) {
                System.out.println("Error sending message: " + e.getMessage());
                e.printStackTrace();
                return false;
            }
        } else {
            System.out.println("Not connected to WebSocket server!");
            return false;
        }
    }

    // Check if connected
    public boolean isConnected() {
        return isConnected && stompSession != null && stompSession.isConnected();
    }


    /**
     * Send a notification to the WebSocket server
     * @param from The sender of the notification
     * @param to The recipient of the notification
     * @return true if notification was sent successfully, false otherwise
     */
    public boolean sendNotification(String from, String to) {
        if (stompSession != null && stompSession.isConnected()) {
            try {
                // Create notification object
                NotificationDTO notification = new NotificationDTO(from, to);

                // Send notification to the notifications endpoint
                stompSession.send("/app/notifications", notification);
                System.out.println("Sent notification from " + from + " to " + to);
                return true;

            } catch (Exception e) {
                System.out.println("Error sending notification: " + e.getMessage());
                e.printStackTrace();
                return false;
            }
        } else {
            System.out.println("Not connected to WebSocket server!");
            return false;
        }
    }

    /**
     * Subscribe to notifications topic to receive incoming notifications
     * Call this method after successful connection to start receiving notifications
     */
    public void subscribeToNotifications() {
        if (stompSession != null && stompSession.isConnected()) {
            stompSession.subscribe(TOPIC_NOTIFICATIONS, new StompFrameHandler() {
                @Override
                public Type getPayloadType(StompHeaders headers) {
                    return NotificationDTO.class;
                }

                @Override
                public void handleFrame(StompHeaders headers, Object payload) {
                    NotificationDTO receivedNotification = (NotificationDTO) payload;
                    Platform.runLater(() -> {
                        System.out.println("Received notification from " + receivedNotification.getFrom() +
                                " to " + receivedNotification.getTo());
                        // TODO: ajouter logique pour intégration dans l'ui
                    });
                }
            });
            System.out.println("Subscribed to notifications");
        } else {
            System.out.println("Cannot subscribe to notifications: not connected to WebSocket server");
        }
    }

    // Disconnect from the WebSocket server
    public void disconnect() {
        if (stompSession != null && stompSession.isConnected()) {
            stompSession.disconnect();
            isConnected = false;
            System.out.println("Disconnected from WebSocket server");
        }
    }
}