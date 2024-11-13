package com.javadiscordprojet.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController  // Indique que c'est un contrôleur REST
public class UserController {
    
    @GetMapping("/test")  // Répond aux requêtes GET sur /test
    public String test() {
        return "Spring Boot is running!";
    }
}