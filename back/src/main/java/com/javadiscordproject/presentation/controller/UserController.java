package com.javadiscordproject.presentation.controller;

import com.javadiscordproject.business.service.implementation.UserMapper;
import com.javadiscordproject.business.service.implementation.UserServiceImpl;
import com.javadiscordproject.domain.model.UserModel;
import com.javadiscordproject.presentation.dto.UserDTO;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    
	private UserServiceImpl userServiceImpl;
	private UserMapper userMapper;
	
	public UserController(UserServiceImpl userServiceImpl, UserMapper userMapper) {
		this.userServiceImpl = userServiceImpl;
		this.userMapper = userMapper;
	}
	
	@PostMapping("/user")
    public void registerUser(@Valid @RequestBody UserDTO userDTO) {
        userServiceImpl.registerUser(userMapper.toModel(userDTO));
    }
	
	@GetMapping("/getUser")
	public ResponseEntity<UserModel> findUserByUsername(@RequestParam String username) {
		UserModel user = userServiceImpl.findByUsername(username);
	    if (user != null) {
	        return ResponseEntity.ok(user);
	    }
	    return ResponseEntity.notFound().build();
	}
		
	@GetMapping("/test")
	public void test() {
		
	}
	
	@GetMapping("/testTokenJwt")
	public void testTokenJwt() {
		
	}
}