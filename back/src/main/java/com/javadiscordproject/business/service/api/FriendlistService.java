package com.javadiscordproject.business.service.api;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.javadiscordproject.domain.model.FriendlistId;
import com.javadiscordproject.domain.model.FriendlistModel;

public interface FriendlistService {
	//public void sendFriendRequest(FriendlistModel friendlistModel);
	public void sendFriendRequest(FriendlistId friendlistId);
	public void sendFriendRequestByUsername(Long idRequester, String username);
	public void acceptFriendRequest(FriendlistId friendlistId);
	//public void refuseFriendRequest(FriendlistModel friendlistModel);
	public void refuseFriendRequest(FriendlistId friendlistId);
	public Optional<FriendlistModel> findFriendRequest(FriendlistModel model);
	public Optional<FriendlistModel> findFriendRequest(Long userId1, Long userId2);
	public Optional<FriendlistModel> findFriendRequest(FriendlistId friendlistId);
	public void deleteFriend(Long userId1, Long userId2);
	public List<FriendlistModel> getFriendlistByStatus(Long userId, String status);
	public Set<Long> getUsersIdFromFriendlist(Long userId);
	public Set<String> getUsersUsernameFromFriendlist(Long userId);
	public Set<String> getPendingFriendRequestsReturnUsername(Long userId);
}
