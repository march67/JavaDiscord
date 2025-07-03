package com.javadiscordproject.services.api.friend;

import com.javadiscordproject.models.FriendDTO;
import com.javadiscordproject.services.api.friend.FriendService.AddFriendResult;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IFriendService {
    CompletableFuture<List<FriendDTO>> getFriendsList();
    CompletableFuture<AddFriendResult> addFriend(String friendUsername);
    CompletableFuture<Boolean> removeFriend(String friendId);
}