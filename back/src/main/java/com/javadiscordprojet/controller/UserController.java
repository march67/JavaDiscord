package com.javadiscordprojet.controller;

import com.javadiscordprojet.repository.TestRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController  // Indique que c'est un contrôleur REST
public class UserController {
    
	private final TestRepository testRepository;
	
	public UserController(TestRepository testRepository) {
		this.testRepository = testRepository;
	}
	
    @GetMapping("/test")  // Répond aux requêtes GET sur /test
    public String test() {
        try {
            testRepository.insertTest("test50");
            return "Insertion réussie !";
        } catch (Exception e) {
            return "Erreur lors de l'insertion : " + e.getMessage();
        }
    }
}