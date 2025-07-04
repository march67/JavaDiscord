package com.javadiscordproject.services.websocket;

import com.javadiscordproject.managers.SessionManager;
import com.javadiscordproject.models.ChatMessageDTO;
import com.javadiscordproject.models.MessageWSDTO;
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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

import static com.javadiscordproject.utils.WsConstants.*;

public class WebSocketService {

    private StompSession stompSession;
    private boolean isConnected = false;
    private static final int CONNECTION_TIMEOUT_SECONDS = 3;

    private Map<Long, StompSession.Subscription> conversationSubscriptions = new HashMap<>();
    private Consumer<MessageWSDTO> messageReceivedCallback;

    public WebSocketService() {}

    public boolean connectToWebSocket() {
        try {
            StandardWebSocketClient webSocketClient = new StandardWebSocketClient();
            WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);

            stompClient.setMessageConverter(new MappingJackson2MessageConverter());

            StompSessionHandler sessionHandler = new CustomStompSessionHandler();

            WebSocketHttpHeaders httpHeaders = new WebSocketHttpHeaders();
            String jwtToken = SessionManager.getInstance().getToken();
            httpHeaders.add(AUTHORIZATION_HEADER, BEARER_PREFIX + jwtToken);

            StompHeaders connectHeaders = new StompHeaders();
            connectHeaders.add(AUTHORIZATION_HEADER, BEARER_PREFIX + jwtToken);

            stompSession = stompClient
                    .connectAsync(WEBSOCKET_URL, httpHeaders, connectHeaders, sessionHandler)
                    .get(CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS);

            if (stompSession != null && stompSession.isConnected()) {
                isConnected = true;
                return true;
            } else {
                isConnected = false;
                return false;
            }

        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            isConnected = false;
            return false;
        }
    }

    public void subscribeToConversation(Long conversationId) {
        if (stompSession != null && stompSession.isConnected()) {
            unsubscribeFromAllConversations();

            String destination = TOPIC_MESSAGES + "/" + conversationId;

            StompSession.Subscription subscription = stompSession.subscribe(destination, new StompFrameHandler() {
                @Override
                public Type getPayloadType(StompHeaders headers) {
                    return MessageWSDTO.class;
                }

                @Override
                public void handleFrame(StompHeaders headers, Object payload) {
                    MessageWSDTO receivedMsg = (MessageWSDTO) payload;
                    Platform.runLater(() -> {
                        if (messageReceivedCallback != null) {
                            messageReceivedCallback.accept(receivedMsg);
                        }
                    });
                }
            });

            conversationSubscriptions.put(conversationId, subscription);
        }
    }

    public void unsubscribeFromAllConversations() {
        for (Map.Entry<Long, StompSession.Subscription> entry : conversationSubscriptions.entrySet()) {
            entry.getValue().unsubscribe();
        }
        conversationSubscriptions.clear();
    }

    public boolean sendMessageToConversation(Long conversationId, String sender, String content) {
        if (stompSession != null && stompSession.isConnected()) {
            try {
                String timestamp = new SimpleDateFormat(TIMESTAMP_FORMAT).format(new Date());
                MessageWSDTO message = new MessageWSDTO(sender, content, timestamp, conversationId);

                String destination = APP_CHAT_ENDPOINT + conversationId;

                stompSession.send(destination, message);
                System.out.println("=== SENDING MESSAGE ===");
                System.out.println("Destination: " + destination);
                System.out.println("Conversation ID: " + conversationId);
                System.out.println("Sender: " + sender);
                System.out.println("Content: " + content);
                System.out.println("Timestamp: " + timestamp);
                System.out.println("=====================");
                return true;

            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }

    public void setMessageReceivedCallback(Consumer<MessageWSDTO> callback) {
        this.messageReceivedCallback = callback;
    }

    public boolean isConnected() {
        return isConnected && stompSession != null && stompSession.isConnected();
    }

    public boolean sendNotification(String from, String to) {
        if (stompSession != null && stompSession.isConnected()) {
            try {
                NotificationDTO notification = new NotificationDTO(from, to);
                stompSession.send(APP_NOTIFICATIONS_ENDPOINT, notification);
                return true;

            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }

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
                        // Handle notification silently
                    });
                }
            });
        }
    }

    public void disconnect() {
        if (stompSession != null && stompSession.isConnected()) {
            unsubscribeFromAllConversations();
            stompSession.disconnect();
            isConnected = false;
        }
    }
}