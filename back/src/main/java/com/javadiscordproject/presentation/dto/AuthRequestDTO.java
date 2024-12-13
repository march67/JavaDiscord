package com.javadiscordproject.presentation.dto;

public class AuthRequestDTO {
    private String username;
    private String password;
    
    // getters, setters
    public String getUsername() {
    	return this.username;
    }
    
    public String getPassword() {
    	return this.password;
    }
    
    public void setUsername(String username) {
    	this.username = username;
    }
    
    public void setPassword(String password) {
    	this.password = password;
    }
}