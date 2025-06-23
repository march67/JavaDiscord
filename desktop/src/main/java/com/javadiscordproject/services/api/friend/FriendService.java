package com.javadiscordproject.services.api.friend;

import com.javadiscordproject.models.FriendDTO;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FriendService implements IFriendService {

    private final List<FriendDTO> friends = Arrays.asList(
            new FriendDTO("Alice"),
            new FriendDTO("Bob"),
            new FriendDTO("Charlie"),
            new FriendDTO("Diana"),
            new FriendDTO("Eve")
    );

    @Override
    public CompletableFuture<List<FriendDTO>> getFriendsList() {
        return CompletableFuture.completedFuture(friends);
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