package com.javadiscordproject.services.api.friend;

import com.javadiscordproject.models.FriendDTO;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IFriendRequestService {
    CompletableFuture<List<FriendDTO>> getPendingFriendRequests();
    CompletableFuture<Boolean> acceptFriendRequest(String username);
    CompletableFuture<Boolean> declineFriendRequest(String username);
}