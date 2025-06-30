package com.javadiscordproject.services.api.friend;

import com.javadiscordproject.managers.SessionManager;
import com.javadiscordproject.models.FriendDTO;
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
    public CompletableFuture<List<FriendDTO>> getPendingFriendRequests() {
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
                    List<FriendDTO> pendingRequests = new ArrayList<>();
                    for (String username : pendingUsernames) {
                        pendingRequests.add(new FriendDTO(username));
                    }
                    return pendingRequests;
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
        // TODO: implement API call when endpoint is available
        return CompletableFuture.completedFuture(true);
    }

    @Override
    public CompletableFuture<Boolean> declineFriendRequest(String username) {
        // TODO: implement API call when endpoint is available
        return CompletableFuture.completedFuture(true);
    }
}