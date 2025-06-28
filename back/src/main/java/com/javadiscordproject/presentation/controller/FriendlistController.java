package com.javadiscordproject.presentation.controller;

import java.util.Map;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javadiscordproject.business.service.api.FriendlistService;
import com.javadiscordproject.business.service.api.UserService;
import com.javadiscordproject.domain.model.FriendlistId;
import com.javadiscordproject.infrastructure.security.jwt.JwtTokenUtil;
import com.javadiscordproject.mapper.FriendlistMapper;
import com.javadiscordproject.presentation.dto.Friendlist.FriendRequestByIdDTO;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/friendlist")
public class FriendlistController {
	private FriendlistService friendlistService;
	private UserService userService;
	private FriendlistMapper friendlistMapper;
	private JwtTokenUtil jwtTokenUtil;
	
	public FriendlistController(FriendlistService friendListService, FriendlistMapper friendlistMapper, JwtTokenUtil jwtTokenUtil,
			UserService userService)
	{
		this.friendlistService = friendListService;
		this.friendlistMapper = friendlistMapper;
		this.jwtTokenUtil = jwtTokenUtil;
		this.userService = userService;
	}
	
	@PostMapping("/add-friend/by-id/{receiverId}")
	public ResponseEntity<Void> addFriendById(@PathVariable Long receiverId, HttpServletRequest request) {
		// Retrieve the user ID of the friend request sender from the claims
		String authHeader = request.getHeader("Authorization");
		String token = authHeader.substring(7); // delete "Bearer " string		
		Long requesterId = jwtTokenUtil.extractUserId(token);
		
		FriendlistId id = new FriendlistId(requesterId, receiverId);
				
		// The friend request receiver user Id is taken from the body 
		friendlistService.sendFriendRequest(id, requesterId);
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping("/add-friend/by-username/{username}")
	public ResponseEntity<Void> addFriendByUsername(@PathVariable String username, HttpServletRequest request) {
		// Retrieve the user ID of the friend request sender from the claims
		String authHeader = request.getHeader("Authorization");
		String token = authHeader.substring(7); // delete "Bearer " string	
		Long requesterId = jwtTokenUtil.extractUserId(token);
		
		friendlistService.sendFriendRequestByUsername(requesterId, username);

		return ResponseEntity.noContent().build();
	}
	
	
	@PostMapping("/refuse/{requesterUsername}")
	public ResponseEntity<Void> refuse(@PathVariable String requesterUsername, HttpServletRequest request) {
		// Retrieve the user ID of the friend request sender from the claims
		String authHeader = request.getHeader("Authorization");
		String token = authHeader.substring(7);	
		Long receiverId = jwtTokenUtil.extractUserId(token);
		Long requesterId = userService.getIdByUsername(requesterUsername);
		
		FriendlistId id = new FriendlistId(receiverId, requesterId);
		
		friendlistService.refuseFriendRequest(id);
		
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping("/accept/{username}")
	public ResponseEntity<Void> accept(@PathVariable String username, HttpServletRequest request) {
		// Retrieve the user ID of the friend request sender from the claims
		String authHeader = request.getHeader("Authorization");
		String token = authHeader.substring(7);	
		Long requesterId = jwtTokenUtil.extractUserId(token);
		Long userId = userService.getIdByUsername(username);
		
		FriendlistId id = new FriendlistId(requesterId, userId);
		
		friendlistService.acceptFriendRequest(id, requesterId);
		
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/delete/{friendId}")
	public ResponseEntity<Void> delete(@PathVariable Long friendId, HttpServletRequest request) {		
		// Retrieve the user ID of the friend request sender from the claims
		String authHeader = request.getHeader("Authorization");
		String token = authHeader.substring(7);
		
		Long userId_2 = jwtTokenUtil.extractUserId(token);
        
		friendlistService.deleteFriend(friendId, userId_2);
		
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/friendlist")
	public ResponseEntity<Set<String>> getFriendlist(HttpServletRequest request) {
		// Retrieve the user ID of the friend request sender from the claims
		String authHeader = request.getHeader("Authorization");
		String token = authHeader.substring(7);
		Long userId = jwtTokenUtil.extractUserId(token);
		
		Set<String> friendlistUsersUsername = friendlistService.getUsersUsernameFromFriendlist(userId);
		
		return ResponseEntity.ok(friendlistUsersUsername);
	}
	
	@GetMapping("/pending-friend-requests")
	public ResponseEntity<Set<String>> getPendingFriendRequests(HttpServletRequest request) {
		// Retrieve the user ID of the friend request sender from the claims
		String authHeader = request.getHeader("Authorization");
		String token = authHeader.substring(7);
		Long userId = jwtTokenUtil.extractUserId(token);
		
		Set<String> friendRequests = friendlistService.getPendingFriendRequestsReturnUsername(userId);
		return ResponseEntity.ok(friendRequests);
	}
}
