package com.javadiscordproject.business.service.implementation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.javadiscordproject.business.service.api.ConversationService;
import com.javadiscordproject.business.service.api.FriendlistService;
import com.javadiscordproject.business.service.api.UserConversationService;
import com.javadiscordproject.business.service.api.UserService;
import com.javadiscordproject.domain.model.ConversationModel;
import com.javadiscordproject.domain.model.FriendlistId;
import com.javadiscordproject.domain.model.FriendlistModel;
import com.javadiscordproject.domain.model.UserConversationModel;
import com.javadiscordproject.domain.model.UserModel;
import com.javadiscordproject.infrastructure.persistence.repository.FriendlistRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class FriendlistServiceImpl implements FriendlistService {
	private FriendlistRepository friendlistRepository;
	private UserService userService;
	private UserConversationService userConversationService;
	private ConversationService conversationService;
	
	public record UserPair(UserModel requester, UserModel receiver) {}
	
	public FriendlistServiceImpl(FriendlistRepository friendlistRepository, UserService userService, UserConversationService userConversationService
			, ConversationService conversationService) {
		this.friendlistRepository = friendlistRepository;
		this.userService = userService;
		this.userConversationService = userConversationService;
		this.conversationService = conversationService;
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
		
		// Already friend case
		if (areFriends(user1, user2)) throw new IllegalArgumentException("Users " + user1.getUsername() + " " + user2.getUsername() + " are already friends");
		
        // Auto accept friend request if a request from the receiver is already sent to the requester
        if (autoAcceptFriendRequest(user1, user2, requesterId)) return;
		
		entity.setUsers(user1, user2);
		
		// set status + requester and save
		entity.setFriendRequest_status("pending");
		entity.setRequesterId(requesterId);
		friendlistRepository.save(entity);
	}
	
	@Override
	public void sendFriendRequestByUsername(Long requesterId, String username) {
		FriendlistModel friendlistModel = new FriendlistModel();
		
    	// Search for the two existing users
    	UserModel user1 = userService.findUserById(requesterId);
    	UserModel user2 = userService.findUserByUsername(username);
    	
    	// Requester is accepting his own request case
    	if (user1.getId() == user2.getId()) {
    		throw new IllegalArgumentException("User cannot friend request himself as a friend");
    	}
        
    	// Already friend case
        if (areFriends(user1, user2)) throw new IllegalArgumentException("Users " + user1.getUsername() + " " + user2.getUsername() + " are already friends");
        
        // Auto accept friend request if a request from the receiver is already sent to the requester
        if (autoAcceptFriendRequest(user1, user2, requesterId)) return;
        
        // Set Users(id) + Status + RequesterId then save
        friendlistModel.setUsers(user1, user2);
        friendlistModel.setFriendRequest_status("pending");
        friendlistModel.setRequesterId(requesterId);
        friendlistRepository.save(friendlistModel);
	}
	
	@Override
	public void acceptFriendRequest(FriendlistId friendlistId, Long requesterId) {
		FriendlistModel entity = findFriendRequest(friendlistId);
		
		if ( !(requesterId == entity.getRequesterId())) {
			entity.setFriendRequest_status("accepted");
			friendlistRepository.save(entity);
			
			// create new conversation between the two users
			ConversationModel conversation = new ConversationModel();
			conversationService.saveConversation(conversation);
			
			UserModel user1 = userService.findUserById(friendlistId.getUserId_1());
			UserModel user2 = userService.findUserById(friendlistId.getUserId_2());
			UserConversationModel userConversation1 = new UserConversationModel(user1, conversation);
			UserConversationModel userConversation2 = new UserConversationModel(user2, conversation);
			userConversationService.saveUserConversation(userConversation1);
			userConversationService.saveUserConversation(userConversation2);
		}
		else {
			throw new IllegalArgumentException("The requester cannot accept the request sent by himself");
		}
	}
	
	private boolean autoAcceptFriendRequest(UserModel user1, UserModel user2, Long requesterId) {
		UserPair pair = resolveRequesterAndReceiver(user1, user2, requesterId);
		UserModel userRequester = pair.requester();
		UserModel userReceiver = pair.receiver();

		
        for ( String pendingUsername : getPendingFriendRequestsReturnUsername(userRequester.getId())) {
        	if(pendingUsername.equals(userReceiver.getUsername())) {
        		FriendlistId friendlistId = findFriendRequest(user1.getId(), user2.getId()).getId();
        		acceptFriendRequest(friendlistId, requesterId);
        		return true;
        	}
        };
        
        return false;
	}
	
	private UserPair resolveRequesterAndReceiver(UserModel user1, UserModel user2, Long requesterId) {
		if (user1.getId().equals(requesterId)) {
			return new UserPair(user1, user2);
		} else if (user2.getId().equals(requesterId)) {
			return new UserPair(user2, user1);
		} else {
			throw new IllegalArgumentException("The requesterId doesn't match with the id of user1 or user 2");
		}
	}

	@Override
	public void refuseFriendRequest(FriendlistId friendlistId) {
		FriendlistModel entity = findFriendRequest(friendlistId);
		friendlistRepository.delete(entity);
	}

	@Override
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
	
	public boolean areFriends(UserModel user1, UserModel user2) {
		// Search in this order first
		if ( friendlistRepository.findByUserId1AndUserId2AndFriendRequestStatus(user1.getId(), user2.getId(), "accepted").isPresent()) {
			return true;
		};
		
		// If first search not found, search in the other order
		return friendlistRepository.findByUserId1AndUserId2AndFriendRequestStatus(user2.getId(), user1.getId(), "accepted").isPresent();
	}
}
