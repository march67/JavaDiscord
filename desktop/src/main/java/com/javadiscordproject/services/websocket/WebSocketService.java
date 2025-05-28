package com.javadiscordproject.services.websocket;

import com.javadiscordproject.models.ChatMessageDTO;
import com.javadiscordproject.utils.CustomStompSessionHandler;
import javafx.application.Platform;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
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

    // Constructor
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
            try {
                stompSession = stompClient.connectAsync(WEBSOCKET_URL, sessionHandler)
                        .get(CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS);
            } catch (TimeoutException e) {
                System.out.println("Connection timed out after " + CONNECTION_TIMEOUT_SECONDS + " seconds");
                isConnected = false;
                return false;
            }

            // If we got here, we're connected
            if (stompSession != null && stompSession.isConnected()) {
                // Subscribe to the topic
                stompSession.subscribe(TOPIC_ENDPOINT, new StompFrameHandler() {
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

    // TODO: remove this
    // Send the predefined static message
    public boolean sendStaticMessage() {
        return sendMessage("JavaFX Client", "Hello World!");
    }

    // Check if connected
    public boolean isConnected() {
        return isConnected && stompSession != null && stompSession.isConnected();
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