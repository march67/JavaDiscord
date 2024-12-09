package com.javadiscordproject.presentation.controller;

import com.javadiscordproject.business.service.api.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    
	private final UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
    @GetMapping("/test")
    public String test() {
        try {
        	userService.insertTest("usera54");
            return "Insert has succeeded";
        } catch (Exception e) {
            return "Insertion error : " + e.getMessage();
        }
    }
}