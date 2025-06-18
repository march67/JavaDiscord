package com.javadiscordproject.business.service.api;

import java.util.Optional;

import com.javadiscordproject.domain.model.FriendlistModel;

public interface FriendlistService {
	public void sendFriendRequest(FriendlistModel friendlistModel);
	public void acceptFriendRequest(FriendlistModel friendlistModel);
	public void refuseFriendRequest(FriendlistModel friendlistModel);
	public Optional<FriendlistModel> findFriendRequest(FriendlistModel model);
}
