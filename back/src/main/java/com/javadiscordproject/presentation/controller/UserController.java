package com.javadiscordproject.presentation.controller;

import com.javadiscordproject.business.service.api.UserService;
import com.javadiscordproject.domain.model.UserModel;
import com.javadiscordproject.presentation.dto.UserDTO;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    
	private final UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@PostMapping("/users")
    public UserModel register(UserDTO userDTO) {
        return userService.createUser(userDTO);
    }
}