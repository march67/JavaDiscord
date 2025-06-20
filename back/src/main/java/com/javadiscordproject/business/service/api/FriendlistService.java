package com.javadiscordproject.business.service.api;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.javadiscordproject.domain.model.FriendlistModel;

public interface FriendlistService {
	public void sendFriendRequest(FriendlistModel friendlistModel);
	public void acceptFriendRequest(FriendlistModel friendlistModel);
	public void refuseFriendRequest(FriendlistModel friendlistModel);
	public Optional<FriendlistModel> findFriendRequest(FriendlistModel model);
	public Optional<FriendlistModel> findFriendRequest(Long userId1, Long userId2);
	public void deleteFriend(Long userId1, Long userId2);
	public Set<Long> getUsersIdFromFriendlist(Long userId);
	public Set<String> getUsersUsernameFromFriendlist(Long userId);
}
