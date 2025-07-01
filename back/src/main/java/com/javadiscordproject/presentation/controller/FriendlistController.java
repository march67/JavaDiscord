package com.javadiscordproject.presentation.controller;

import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javadiscordproject.business.service.api.FriendlistService;
import com.javadiscordproject.business.service.api.UserService;

import com.javadiscordproject.domain.model.FriendlistId;
import com.javadiscordproject.infrastructure.security.jwt.CustomUserDetails;

@RestController
@RequestMapping("/friendlist")
public class FriendlistController {
	private FriendlistService friendlistService;
	private UserService userService;
	
	public FriendlistController(FriendlistService friendListService, UserService userService)
	{
		this.friendlistService = friendListService;
		this.userService = userService;
	}
	
	@PostMapping("/add-friend/by-id/{receiverId}")
	public ResponseEntity<Void> addFriendById(@PathVariable Long receiverId, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
		// Retrieve the user ID of the friend request sender from the claims
		Long requesterId = customUserDetails.getId();
		
		FriendlistId id = new FriendlistId(requesterId, receiverId);
				
		// The friend request receiver user Id is taken from the body 
		friendlistService.sendFriendRequest(id, requesterId);
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping("/add-friend/by-username/{username}")
	public ResponseEntity<Void> addFriendByUsername(@PathVariable String username, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
		// Retrieve the user ID of the friend request sender from the claims
		Long requesterId = customUserDetails.getId();
		
		friendlistService.sendFriendRequestByUsername(requesterId, username);

		return ResponseEntity.noContent().build();
	}
	
	
	@PostMapping("/refuse/{requesterUsername}")
	public ResponseEntity<Void> refuse(@PathVariable String requesterUsername, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
		// Retrieve the user ID of the friend request sender from the claims
		Long receiverId = customUserDetails.getId();
		
		Long requesterId = userService.getIdByUsername(requesterUsername);
		
		FriendlistId id = new FriendlistId(receiverId, requesterId);
		
		friendlistService.refuseFriendRequest(id);
		
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping("/accept/{username}")
	public ResponseEntity<Void> accept(@PathVariable String username, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
		// Retrieve the user ID of the friend request sender from the claims
		Long requesterId = customUserDetails.getId();
		
		Long userId = userService.getIdByUsername(username);
		
		FriendlistId id = new FriendlistId(requesterId, userId);
		
		friendlistService.acceptFriendRequest(id, requesterId);
		
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/{friendUsername}")
	public ResponseEntity<Void> delete(@PathVariable String friendUsername, @AuthenticationPrincipal CustomUserDetails customUserDetails) {		
		// Retrieve the user ID of the friend request sender from the claims
		Long userId_2 = customUserDetails.getId();
        
		friendlistService.deleteFriend(friendUsername, userId_2);
		
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping()
	public ResponseEntity<Set<String>> getFriendlist(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		// Retrieve the user ID of the friend request sender from the claims
		Long userId = customUserDetails.getId();
		
		Set<String> friendlistUsersUsername = friendlistService.getUsersUsernameFromFriendlist(userId);
		
		return ResponseEntity.ok(friendlistUsersUsername);
	}
	
	@GetMapping("/pending-friend-requests")
	public ResponseEntity<Set<String>> getPendingFriendRequests(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		// Retrieve the user ID of the friend request sender from the claims
		Long userId = customUserDetails.getId();
		
		Set<String> friendRequests = friendlistService.getPendingFriendRequestsReturnUsername(userId);
		return ResponseEntity.ok(friendRequests);
	}
}
