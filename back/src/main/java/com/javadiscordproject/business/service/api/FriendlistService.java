package com.javadiscordproject.business.service.api;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.javadiscordproject.domain.model.FriendlistId;
import com.javadiscordproject.domain.model.FriendlistModel;

public interface FriendlistService {
	public void sendFriendRequest(FriendlistId friendlistId, Long requesterId);
	public void sendFriendRequestByUsername(Long idRequester, String username);
	public void acceptFriendRequest(FriendlistId friendlistId, Long requesterId);
	public void refuseFriendRequest(FriendlistId friendlistId);
	public FriendlistModel findFriendRequest(FriendlistModel model);
	public FriendlistModel findFriendRequest(Long userId1, Long userId2);
	public FriendlistModel findFriendRequest(FriendlistId friendlistId);
	public void deleteFriend(Long userId1, Long userId2);
	public List<FriendlistModel> getFriendlistByStatus(Long userId, String status);
	public Set<Long> getUsersIdFromFriendlist(Long userId);
	public Set<String> getUsersUsernameFromFriendlist(Long userId);
	public Set<String> getPendingFriendRequestsReturnUsername(Long userId);
}
