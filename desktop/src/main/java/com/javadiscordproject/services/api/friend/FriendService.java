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

public class FriendService implements IFriendService {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public FriendService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
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

                if (response.statusCode() == 200) {
                    List<String> friendNames = objectMapper.readValue(response.body(), new TypeReference<List<String>>() {});
                    List<FriendDTO> friends = new ArrayList<>();
                    for (String name : friendNames) {
                        friends.add(new FriendDTO(name));
                    }
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
    public CompletableFuture<Boolean> addFriend(String friendId) {
        // TODO: implement later
        return CompletableFuture.completedFuture(false);
    }

    @Override
    public CompletableFuture<Boolean> removeFriend(String friendId) {
        // TODO: implement later
        return CompletableFuture.completedFuture(false);
    }
}