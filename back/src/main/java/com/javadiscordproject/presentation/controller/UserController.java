package com.javadiscordproject.presentation.controller;

import com.javadiscordproject.business.service.api.UserService;
import com.javadiscordproject.business.service.implementation.UserServiceImpl;
import com.javadiscordproject.domain.model.UserModel;
import com.javadiscordproject.infrastructure.security.jwt.JwtTokenUtil;
import com.javadiscordproject.mapper.UserMapper;
import com.javadiscordproject.presentation.dto.User.UserDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
	private UserService userService;
	private UserMapper userMapper;
	
	public UserController(UserService userService, UserMapper userMapper) {
		this.userService = userService;
		this.userMapper = userMapper;
	}
	
	@PostMapping("/register")
    public void registerUser(@Valid @RequestBody UserDTO userDTO) {
        userService.saveUser(userMapper.toModel(userDTO));
    }
	
	@GetMapping("/findUserByUsername")
	public ResponseEntity<UserModel> findUserByUsername(@RequestParam String username) {
		UserModel user = userService.findUserByUsername(username);
	    if (user != null) {
	        return ResponseEntity.ok(user);
	    }
	    return ResponseEntity.notFound().build();
	}
		
	@GetMapping("/test")
	public void test() {
	}
}