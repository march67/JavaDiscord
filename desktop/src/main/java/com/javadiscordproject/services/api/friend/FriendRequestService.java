package com.javadiscordproject.services.api.friend;

import com.javadiscordproject.models.FriendDTO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FriendRequestService implements IFriendRequestService {

    private final List<FriendDTO> pendingRequests = new ArrayList<>(Arrays.asList(
            new FriendDTO("John"),
            new FriendDTO("Sarah"),
            new FriendDTO("Mike")
    ));

    @Override
    public CompletableFuture<List<FriendDTO>> getPendingFriendRequests() {
        return CompletableFuture.completedFuture(new ArrayList<>(pendingRequests));
    }

    @Override
    public CompletableFuture<Boolean> acceptFriendRequest(String username) {
        pendingRequests.removeIf(request -> request.getUsername().equals(username));
        return CompletableFuture.completedFuture(true);
    }

    @Override
    public CompletableFuture<Boolean> declineFriendRequest(String username) {
        pendingRequests.removeIf(request -> request.getUsername().equals(username));
        return CompletableFuture.completedFuture(true);
    }
}