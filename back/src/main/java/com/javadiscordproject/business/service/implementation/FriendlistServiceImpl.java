package com.javadiscordproject.business.service.implementation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.javadiscordproject.business.service.api.FriendlistService;
import com.javadiscordproject.business.service.api.UserService;
import com.javadiscordproject.domain.model.FriendlistId;
import com.javadiscordproject.domain.model.FriendlistModel;
import com.javadiscordproject.domain.model.UserModel;
import com.javadiscordproject.infrastructure.persistence.repository.FriendlistRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class FriendlistServiceImpl implements FriendlistService {
	private FriendlistRepository friendlistRepository;
	private UserService userService;
	
	public FriendlistServiceImpl(FriendlistRepository friendlistRepository, UserService userService) {
		this.friendlistRepository = friendlistRepository;
		this.userService = userService;
	}

	@Override
	public void sendFriendRequest(FriendlistId friendlistId, Long requesterId) {
		// Verify first if sender and receiver is not the same user
		if(friendlistId.getUserId_1() == friendlistId.getUserId_2()) {
			throw new IllegalArgumentException("User cannot friend request himself as a friend");
		}
		
		FriendlistModel entity = new FriendlistModel();
		
		// charge users
		UserModel user1 = userService.findUserById(friendlistId.getUserId_1());
		UserModel user2 = userService.findUserById(friendlistId.getUserId_2());
		entity.setUsers(user1, user2);
		
		// set status + requester and save
		entity.setFriendRequest_status("pending");
		entity.setRequesterId(requesterId);
		friendlistRepository.save(entity);
	}
	
	@Override
	public void sendFriendRequestByUsername(Long idRequester, String username) {
		FriendlistModel friendlistModel = new FriendlistModel();
		
    	// Search for the two existing users
    	UserModel user1 = userService.findUserById(idRequester);
    	UserModel user2 = userService.findUserByUsername(username);
    	if (user1.getId() == user2.getId()) {
    		throw new IllegalArgumentException("User cannot friend request himself as a friend");
    	}
        friendlistModel.setUsers(user1, user2);
        
        // Set status + requester and save
        friendlistModel.setFriendRequest_status("pending");
        friendlistModel.setRequesterId(idRequester);
        friendlistRepository.save(friendlistModel);
	}
	
	@Override
	public void acceptFriendRequest(FriendlistId friendlistId, Long requesterId) {
		FriendlistModel entity = findFriendRequest(friendlistId);
		
		if ( !(requesterId == entity.getRequesterId())) {
			entity.setFriendRequest_status("accepted");
			friendlistRepository.save(entity);
		}
		else {
			throw new IllegalArgumentException("The requester cannot accept the request sent by himself");
		}
	}

	@Override
	public void refuseFriendRequest(FriendlistId friendlistId) {
		FriendlistModel entity = findFriendRequest(friendlistId);
		friendlistRepository.delete(entity);
	}

	@Override
	//public Optional<FriendlistModel> findFriendRequest(FriendlistModel friendlistModel) {
	public FriendlistModel findFriendRequest(FriendlistModel friendlistModel) {
		FriendlistId id = new FriendlistId(friendlistModel.getUserId_1(), friendlistModel.getUserId_2());
		FriendlistModel entity = friendlistRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Friend request not found between user 1 = " + id.getUserId_1() + " user 2 = " + id.getUserId_2()));
		return entity;
	}
	
	@Override
	public FriendlistModel findFriendRequest(Long userId1, Long userId2) {		
		FriendlistId id = new FriendlistId(userId1, userId2);
		FriendlistModel entity = friendlistRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Friend request not found between user 1 = " + userId1 + " user 2 = " + userId2 ));
		return entity;
	}
	
	@Override
	public FriendlistModel findFriendRequest(FriendlistId id) {		
		FriendlistModel entity = friendlistRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Friend request not found between user 1 = " + id.getUserId_1() + " user 2 = " + id.getUserId_2()));
		return entity;
	}


	@Override
	public void deleteFriend(String friendUsername, Long userId2) {
		UserModel friend = userService.findUserByUsername(friendUsername);
		FriendlistModel entity = findFriendRequest(friend.getId(), userId2);
		friendlistRepository.delete(entity);
	}
	
	@Override
	public List<FriendlistModel> getFriendlistByStatus(Long userId, String status) {
		// Merging result of both where current user is in column 1 & or column 2
		List<FriendlistModel> friendlist_part_1 = friendlistRepository.findByUserId1AndFriendRequestStatus(userId, status);
		List<FriendlistModel> friendlist_part_2 = friendlistRepository.findByUserId2AndFriendRequestStatus(userId, status);
		List<FriendlistModel> friendlist = new ArrayList<>(friendlist_part_1);
		
		if (friendlist.isEmpty()) {
			throw new EntityNotFoundException("List is empty with status relation as " + status + " for userId = " + userId);
		}
		
		friendlist.addAll(friendlist_part_2);
		return friendlist;
	}

	@Override
	public Set<Long> getUsersIdFromFriendlist(Long userId) {
		Set<Long> friendIds = new HashSet<>(); // unique id
		
		// Fill the Set, either try to retrieve the first or second user of the relation
		for ( FriendlistModel relation : getFriendlistByStatus(userId, "accepted") ) {
			if (!relation.getId().getUserId_1().equals(userId)) {
				friendIds.add(relation.getId().getUserId_1());
			} else {
				friendIds.add(relation.getId().getUserId_2());
			}
		}
		return friendIds;
	}
	
	@Override
	public Set<String> getPendingFriendRequestsReturnUsername(Long userId) {
		Set<String> friendNames = new HashSet<>();
		
		// Fill the Set, either try to retrieve the first or second user of the relation ( excludes search of the current user himself )
		for ( FriendlistModel relation : getFriendlistByStatus(userId, "pending") ) {
			// Only get pending request from other requester, not the request made by the user itself
			if ( !(relation.getRequesterId().equals(userId))) {
				// Get the username of the requester
				if (!relation.getId().getUserId_1().equals(userId)) {
					friendNames.add(relation.getUserName_1());
				} else {
					friendNames.add(relation.getUserName_2());
				}
			}
		}
		return friendNames;
	}

	
	@Override
	public Set<String> getUsersUsernameFromFriendlist(Long userId) {
		Set<String> usersName = new HashSet<String>();
		
		// Fill the Set, either try to retrieve the first or second user of the relation ( excludes search of the current user himself )
		for ( FriendlistModel relation : getFriendlistByStatus(userId, "accepted") ) {
			if (!relation.getId().getUserId_1().equals(userId)) {
				usersName.add(relation.getUserName_1());
			} else {
				usersName.add(relation.getUserName_2());
			}
		}

		return usersName;
	}
}
