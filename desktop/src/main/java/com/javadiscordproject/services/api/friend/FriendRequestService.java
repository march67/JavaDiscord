package com.javadiscordproject.services.api.friend;

import com.javadiscordproject.managers.SessionManager;
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

public class FriendRequestService implements IFriendRequestService {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public FriendRequestService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public CompletableFuture<List<String>> getPendingFriendRequests() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String token = SessionManager.getInstance().getToken();

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(ApiConstants.PENDING_FRIEND_REQUESTS_ENDPOINT))
                        .header("Authorization", "Bearer " + token)
                        .header("Accept", "application/json")
                        .GET()
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    List<String> pendingUsernames = objectMapper.readValue(response.body(), new TypeReference<List<String>>() {});
                    return pendingUsernames;
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
    public CompletableFuture<Boolean> acceptFriendRequest(String username) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String token = SessionManager.getInstance().getToken();

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(ApiConstants.ACCEPT_FRIEND_REQUEST_ENDPOINT + "/" + username))
                        .header("Authorization", "Bearer " + token)
                        .header("Accept", "*/*")
                        .POST(HttpRequest.BodyPublishers.noBody())
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                return response.statusCode() == 204;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    @Override
    public CompletableFuture<Boolean> declineFriendRequest(String username) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String token = SessionManager.getInstance().getToken();

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(ApiConstants.REFUSE_FRIEND_REQUEST_ENDPOINT + "/" + username))
                        .header("Authorization", "Bearer " + token)
                        .header("Accept", "*/*")
                        .POST(HttpRequest.BodyPublishers.noBody())
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                return response.statusCode() == 204;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });
    }
}