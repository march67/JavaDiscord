package com.javadiscordprojet.presentation.controller;

import com.javadiscordprojet.infrastructure.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    
	private final UserRepository userRepository;
	
	public UserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
    @GetMapping("/test")
    public String test() {
        try {
            userRepository.insertTest("usera54");
            return "Insert has succeeded";
        } catch (Exception e) {
            return "Insertion error : " + e.getMessage();
        }
    }
}