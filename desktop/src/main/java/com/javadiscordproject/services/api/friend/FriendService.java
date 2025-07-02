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
    public CompletableFuture<Boolean> addFriend(String friendUsername) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String token = SessionManager.getInstance().getToken();

                String url = ApiConstants.ADD_FRIEND_ENDPOINT + "/" + friendUsername;

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Authorization", "Bearer " + token)
                        .header("Accept", "*/*")
                        .POST(HttpRequest.BodyPublishers.noBody())
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                return response.statusCode() >= 200 && response.statusCode() < 300;

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    @Override
    public CompletableFuture<Boolean> removeFriend(String friendId) {
        // TODO: implement later
        return CompletableFuture.completedFuture(false);
    }
}