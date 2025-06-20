package com.javadiscordproject.business.service.implementation;

import java.time.LocalDateTime;
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

@Service
public class FriendlistServiceImpl implements FriendlistService {
	private FriendlistRepository friendlistRepository;
	private UserService userService;
	
	public FriendlistServiceImpl(FriendlistRepository friendlistRepository, UserService userService) {
		this.friendlistRepository = friendlistRepository;
		this.userService = userService;
	}

	@Override
	public void sendFriendRequest(FriendlistModel friendlistModel) {
		// Verify first if sender and receiver is not the same user
		if(friendlistModel.getUserId_1().equals(friendlistModel.getUserId_2())) {
			throw new IllegalArgumentException("User cannot friend request himself as a friend");
		}
		
		friendlistModel.setFriendRequest_status("pending");
		friendlistRepository.save(friendlistModel);
	}
	
	@Override
	public void sendFriendRequestByUsername(Long idRequester, String username) {
		FriendlistModel friendlistModel = new FriendlistModel();
		
    	// Search for the two existing users
    	UserModel user1 = userService.findUserById(idRequester);
    	UserModel user2 = userService.findUserByUsername(username);
        friendlistModel.setUsers(user1, user2);
        
        // Set status & save relation
        friendlistModel.setFriendRequest_status("pending");
        friendlistRepository.save(friendlistModel);
	}
	
	@Override
	public void acceptFriendRequest(FriendlistModel friendlistModel) {
		findFriendRequest(friendlistModel).ifPresentOrElse(
				entity -> {
					entity.setFriendRequest_status("accepted");
					friendlistRepository.save(entity);
				},
				() -> System.out.println("Friend request not found")
				
			);
	}

	@Override
	public void refuseFriendRequest(FriendlistModel friendlistModel) {
		findFriendRequest(friendlistModel).ifPresentOrElse(
				entity -> {
					friendlistRepository.delete(entity);
				},
				() -> System.out.println("Friend request not found")
				
			);
	}

	@Override
	public Optional<FriendlistModel> findFriendRequest(FriendlistModel friendlistModel) {
		FriendlistId id = new FriendlistId(friendlistModel.getUserId_1(), friendlistModel.getUserId_2());
		Optional<FriendlistModel> entity = friendlistRepository.findById(id);
		return entity;
	}
	
	@Override
	public Optional<FriendlistModel> findFriendRequest(Long userId1, Long userId2) {		
		FriendlistId id = new FriendlistId(userId1, userId2);
		Optional<FriendlistModel> entity = friendlistRepository.findById(id);
		return entity;
	}


	@Override
	public void deleteFriend(Long userId1, Long userId2) {
		findFriendRequest(userId1, userId2).ifPresentOrElse(
				entity -> {
					friendlistRepository.delete(entity);
				},
				() -> System.out.println("Friend relation not found")
			);
	}
	
	@Override
	public List<FriendlistModel> getFriendlistByStatus(Long userId, String status) {
		// Merging result of both where current user is in column 1 & or column 2
		List<FriendlistModel> friendlist_part_1 = friendlistRepository.findByUserId1AndFriendRequestStatus(userId, status);
		List<FriendlistModel> friendlist_part_2 = friendlistRepository.findByUserId2AndFriendRequestStatus(userId, status);
		List<FriendlistModel> friendlist = new ArrayList<>(friendlist_part_1);
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
			if (!relation.getId().getUserId_1().equals(userId)) {
				friendNames.add(relation.getUserName_1());
			} else {
				friendNames.add(relation.getUserName_2());
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
