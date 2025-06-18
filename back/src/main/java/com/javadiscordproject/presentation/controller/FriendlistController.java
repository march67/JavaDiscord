package com.javadiscordproject.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javadiscordproject.business.service.api.FriendlistService;
import com.javadiscordproject.infrastructure.security.jwt.JwtTokenUtil;
import com.javadiscordproject.mapper.FriendlistMapper;
import com.javadiscordproject.presentation.dto.Friendlist.FriendRequestDTO;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/friendlist")
public class FriendlistController {
	private FriendlistService friendListService;
	private FriendlistMapper friendlistMapper;
	private JwtTokenUtil jwtTokenUtil;
	
	public FriendlistController(FriendlistService friendListService, FriendlistMapper friendlistMapper, JwtTokenUtil jwtTokenUtil) {
		this.friendListService = friendListService;
		this.friendlistMapper = friendlistMapper;
		this.jwtTokenUtil = jwtTokenUtil;
	}
	
	@PostMapping("/add-friend")
	public ResponseEntity<FriendRequestDTO> addFriend(@RequestBody FriendRequestDTO friendRequestDTO, HttpServletRequest request) {
		// Retrieve the user ID of the friend request sender from the claims
		String authHeader = request.getHeader("Authorization");
		String token = authHeader.substring(7); // delete "Bearer " string		
		friendRequestDTO.setUserId_1(jwtTokenUtil.extractUserId(token));
				
		// The friend request receiver user Id is taken from the body 
		friendListService.sendFriendRequest(friendlistMapper.toModel(friendRequestDTO));
		return null;
	}
	
	@PostMapping("/refuse")
	public ResponseEntity<Void> refuse(@RequestBody FriendRequestDTO friendRequestDTO, HttpServletRequest request) {
		
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping("/accept")
	public ResponseEntity<Void> accept(@RequestBody FriendRequestDTO friendRequestDTO, HttpServletRequest request) {
		// Retrieve the user ID of the friend request sender from the claims
		String authHeader = request.getHeader("Authorization");
		String token = authHeader.substring(7);	
		friendRequestDTO.setUserId_1(jwtTokenUtil.extractUserId(token));
		
		try {
			friendListService.findFriendRequest(friendlistMapper.toModel(friendRequestDTO));
		} catch (Exception e) {
			System.out.println("Exception levée de type : " + e);
		}
		
		return ResponseEntity.noContent().build();
	}
}
