package com.javadiscordproject.presentation.dto.Auth;

public class AuthResponseDTO {
    private String token;
    
    public AuthResponseDTO(String token) {
        this.token = token;
    }
    
    // getter
    public String getToken() {
    	return this.token;
    }
}