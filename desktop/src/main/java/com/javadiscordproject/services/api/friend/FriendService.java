package com.javadiscordproject.services.api.friend;

import com.javadiscordproject.managers.SessionManager;
import com.javadiscordproject.models.FriendDTO;
import com.javadiscordproject.services.websocket.WebSocketService;
import com.javadiscordproject.utils.ApiConstants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FriendService implements IFriendService {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final WebSocketService webSocketService;

    public FriendService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.webSocketService = WebSocketService.getInstance();
    }

    public FriendService(WebSocketService webSocketService) {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.webSocketService = webSocketService;
    }

    @Override
    public CompletableFuture<List<FriendDTO>> getFriendsList() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String token = SessionManager.getInstance().getToken();

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(ApiConstants.FRIEND_LIST_ENDPOINT))
                        .header("Authorization", "Bearer " + token)
                        .header("Accept", "application/json")
                        .GET()
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println("Response: " + response.body());

                if (response.statusCode() == 200) {
                    List<FriendDTO> friends = objectMapper.readValue(
                            response.body(),
                            new TypeReference<List<FriendDTO>>() {}
                    );
                    return friends;
                } else {
                    return new ArrayList<>();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
        });
    }

    @Override
    public CompletableFuture<AddFriendResult> addFriend(String friendUsername) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String token = SessionManager.getInstance().getToken();
                String currentUsername = SessionManager.getInstance().getUsername();

                String url = ApiConstants.ADD_FRIEND_ENDPOINT + "/" + friendUsername;

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Authorization", "Bearer " + token)
                        .header("Accept", "*/*")
                        .POST(HttpRequest.BodyPublishers.noBody())
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() >= 200 && response.statusCode() < 300) {
                    publishFriendNotification(currentUsername, friendUsername);

                    return new AddFriendResult(true, "Demande d'ami envoyée avec succès!", friendUsername);
                } else if (response.statusCode() == 404) {
                    return new AddFriendResult(false, "L'utilisateur '" + friendUsername + "' n'existe pas", friendUsername);
                } else if (response.statusCode() == 409) {
                    return new AddFriendResult(false, "'" + friendUsername + "' est déjà dans votre liste d'amis", friendUsername);
                } else {
                    return new AddFriendResult(false, "Erreur lors de l'envoi de la demande", friendUsername);
                }

            } catch (Exception e) {
                e.printStackTrace();
                return new AddFriendResult(false, "Erreur de connexion", friendUsername);
            }
        });
    }

    private void publishFriendNotification(String requester, String receiver) {
        try {
            if (!webSocketService.isConnected()) {
                boolean connected = webSocketService.connectToWebSocket();
                if (!connected) {
                    System.err.println("Impossible de se connecter au WebSocket pour envoyer la notification");
                    return;
                }
            }

            boolean sent = webSocketService.sendFriendNotification(requester, receiver);
            if (sent) {
                System.out.println("Notification d'ami envoyée avec succès de " + requester + " vers " + receiver);
            } else {
                System.err.println("Échec de l'envoi de la notification d'ami");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi de la notification WebSocket: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public CompletableFuture<Boolean> removeFriend(String friendId) {
        // TODO: implement later
        return CompletableFuture.completedFuture(false);
    }

    public static class AddFriendResult {
        private final boolean success;
        private final String message;
        private final String username;

        public AddFriendResult(boolean success, String message, String username) {
            this.success = success;
            this.message = message;
            this.username = username;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public String getUsername() {
            return username;
        }
    }
}