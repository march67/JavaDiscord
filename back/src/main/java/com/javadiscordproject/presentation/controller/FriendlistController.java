package com.javadiscordproject.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javadiscordproject.presentation.dto.FriendlistDTO;

@RestController
@RequestMapping("/friendlist")
public class FriendlistController {

	@PostMapping("/addFriend")
	public ResponseEntity<FriendlistDTO> addFriend() {
		return null;
	}
}
