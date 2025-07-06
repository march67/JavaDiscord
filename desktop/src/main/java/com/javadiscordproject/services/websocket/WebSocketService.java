package com.javadiscordproject.services.websocket;

import com.javadiscordproject.managers.SessionManager;
import com.javadiscordproject.models.MessageWSDTO;
import com.javadiscordproject.models.FriendNotificationDTO;
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
    private Consumer<FriendNotificationDTO> notificationReceivedCallback;
    private Consumer<String> friendNotificationReceivedCallback;

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

    public void setNotificationReceivedCallback(Consumer<FriendNotificationDTO> callback) {
        this.notificationReceivedCallback = callback;
    }

    public void setFriendNotificationReceivedCallback(Consumer<String> callback) {
        this.friendNotificationReceivedCallback = callback;
    }

    public boolean isConnected() {
        return isConnected && stompSession != null && stompSession.isConnected();
    }

    public boolean sendFriendNotification(String usernameRequester, String usernameReceiver) {
        if (stompSession != null && stompSession.isConnected()) {
            try {
                FriendNotificationDTO notification = new FriendNotificationDTO(usernameRequester, usernameReceiver);

                String destination = APP_FRIEND_NOTIFICATION_ENDPOINT;

                stompSession.send(destination, notification);

                System.out.println("=== SENDING NOTIFICATION ===");
                System.out.println("Destination: " + destination);
                System.out.println("Requester: " + usernameRequester);
                System.out.println("Receiver: " + usernameReceiver);
                System.out.println("=============================");

                return true;

            } catch (Exception e) {
                System.err.println("Error sending notification: " + e.getMessage());
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean sendFriendNotificationString(String username) {
        if (stompSession != null && stompSession.isConnected()) {
            try {
                stompSession.send(APP_NOTIFICATION_FRIEND_ENDPOINT, username);
                System.out.println("=== FRIEND ACCEPTED NOTIFICATION SENT ===");
                System.out.println("Accepted friend: " + username);
                System.out.println("========================================");
                return true;
            } catch (Exception e) {
                System.err.println("Error sending friend acceptance: " + e.getMessage());
                return false;
            }
        }
        return false;
    }

    public void subscribeToNotifications() {
        if (stompSession != null && stompSession.isConnected()) {
            String destination = TOPIC_FRIEND_NOTIFICATION;

            stompSession.subscribe(destination, new StompFrameHandler() {
                @Override
                public Type getPayloadType(StompHeaders headers) {
                    return FriendNotificationDTO.class;
                }

                @Override
                public void handleFrame(StompHeaders headers, Object payload) {
                    FriendNotificationDTO receivedNotification = (FriendNotificationDTO) payload;
                    Platform.runLater(() -> {
                        System.out.println("=== NOTIFICATION RECEIVED ===");
                        System.out.println("From: " + receivedNotification.getUsername_requester());
                        System.out.println("To: " + receivedNotification.getUsername_receiver());
                        System.out.println("==============================");

                        if (notificationReceivedCallback != null) {
                            notificationReceivedCallback.accept(receivedNotification);
                        }
                    });
                }
            });

            System.out.println("Subscribed to notifications at: " + destination);
        }
    }

    public void subscribeToFriendNotifications() {
        if (stompSession != null && stompSession.isConnected()) {
            stompSession.subscribe(TOPIC_NOTIFICATION_FRIEND, new StompFrameHandler() {
                @Override
                public Type getPayloadType(StompHeaders headers) {
                    return String.class;
                }

                @Override
                public void handleFrame(StompHeaders headers, Object payload) {
                    String receivedUsername = (String) payload;
                    Platform.runLater(() -> {
                        System.out.println("=== FRIEND NOTIFICATION RECEIVED ===");
                        System.out.println("Username: " + receivedUsername);
                        System.out.println("=====================================");

                        if (friendNotificationReceivedCallback != null) {
                            friendNotificationReceivedCallback.accept(receivedUsername);
                        }
                    });
                }
            });

            System.out.println("Subscribed to friend notifications at: " + APP_NOTIFICATION_FRIEND_ENDPOINT);
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