package com.javadiscordproject.business.service.api;

import com.javadiscordproject.domain.model.FriendlistModel;

public interface FriendlistService {
	public void sendFriendRequest(FriendlistModel friendlistModel);
	public void acceptFriendRequest(FriendlistModel friendlistModel);
	public void refuseFriendRequest(FriendlistModel friendlistModel);
}
