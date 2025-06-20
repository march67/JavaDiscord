package com.javadiscordproject.presentation.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import com.javadiscordproject.domain.model.FriendlistModel;
import com.javadiscordproject.domain.model.UserModel;
import com.javadiscordproject.infrastructure.security.jwt.JwtTokenUtil;
import com.javadiscordproject.mapper.FriendlistMapper;
import com.javadiscordproject.presentation.dto.Friendlist.FriendRequestByIdDTO;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/friendlist")
public class FriendlistController {
	private FriendlistService friendlistService;
	private FriendlistMapper friendlistMapper;
	private JwtTokenUtil jwtTokenUtil;
	
	public FriendlistController(FriendlistService friendListService, FriendlistMapper friendlistMapper, JwtTokenUtil jwtTokenUtil) {
		this.friendlistService = friendListService;
		this.friendlistMapper = friendlistMapper;
		this.jwtTokenUtil = jwtTokenUtil;
	}
	
	@PostMapping("/add-friend-by-id")
	public ResponseEntity<Void> addFriendById(@RequestBody FriendRequestByIdDTO friendRequestByIdDTO, HttpServletRequest request) {
		// Retrieve the user ID of the friend request sender from the claims
		String authHeader = request.getHeader("Authorization");
		String token = authHeader.substring(7); // delete "Bearer " string		
		friendRequestByIdDTO.setUserId_1(jwtTokenUtil.extractUserId(token));
				
		// The friend request receiver user Id is taken from the body 
		friendlistService.sendFriendRequest(friendlistMapper.toModel(friendRequestByIdDTO));
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping("/add-friend-by-username")
	public ResponseEntity<Void> addFriendByUsername(@RequestBody Map<String, String> requestBody, HttpServletRequest request) {
		// Retrieve the user ID of the friend request sender from the claims
		String authHeader = request.getHeader("Authorization");
		String token = authHeader.substring(7); // delete "Bearer " string	
		Long idRequester = jwtTokenUtil.extractUserId(token);
		
		friendlistService.sendFriendRequestByUsername(idRequester, requestBody.get("username"));

		return ResponseEntity.noContent().build();
	}
	
	
	@PostMapping("/refuse")
	public ResponseEntity<Void> refuse(@RequestBody FriendRequestByIdDTO friendRequestByIdDTO, HttpServletRequest request) {
		// Retrieve the user ID of the friend request sender from the claims
		String authHeader = request.getHeader("Authorization");
		String token = authHeader.substring(7);	
		friendRequestByIdDTO.setUserId_1(jwtTokenUtil.extractUserId(token));
		
		friendlistService.refuseFriendRequest(friendlistMapper.toModel(friendRequestByIdDTO));
		
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping("/accept")
	public ResponseEntity<Void> accept(@RequestBody FriendRequestByIdDTO friendRequestByIdDTO, HttpServletRequest request) {
		// Retrieve the user ID of the friend request sender from the claims
		String authHeader = request.getHeader("Authorization");
		String token = authHeader.substring(7);	
		friendRequestByIdDTO.setUserId_1(jwtTokenUtil.extractUserId(token));
		
		friendlistService.acceptFriendRequest(friendlistMapper.toModel(friendRequestByIdDTO));
		
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
