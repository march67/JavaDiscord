package com.javadiscordproject.services.api.friend;

import com.javadiscordproject.models.FriendDTO;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IFriendService {
    CompletableFuture<List<FriendDTO>> getFriendsList();
    CompletableFuture<Boolean> addFriend(String friendId);
    CompletableFuture<Boolean> removeFriend(String friendId);
}