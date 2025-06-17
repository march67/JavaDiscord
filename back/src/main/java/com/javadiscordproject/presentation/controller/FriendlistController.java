package com.javadiscordproject.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javadiscordproject.business.service.api.FriendlistService;
import com.javadiscordproject.mapper.FriendlistMapper;
import com.javadiscordproject.presentation.dto.Friendlist.FriendRequestDTO;

@RestController
@RequestMapping("/friendlist")
public class FriendlistController {
	private FriendlistService friendListService;
	private FriendlistMapper friendlistMapper;
	
	public FriendlistController(FriendlistService friendListService, FriendlistMapper friendlistMapper) {
		this.friendListService = friendListService;
		this.friendlistMapper = friendlistMapper;
	}
	
	@PostMapping("/add-friend")
	public ResponseEntity<FriendRequestDTO> addFriend(@RequestBody FriendRequestDTO friendRequestDTO) {
		friendListService.sendFriendRequest(friendlistMapper.toModel(friendRequestDTO));
		return null;
	}
}
