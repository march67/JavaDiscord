package com.javadiscordproject.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

@Entity
@Table(name = "application_user")
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String username;
    private String password;
    

    public Long getApplicationUserId() {
    	return this.id;
    }
    
    // Getters
    public String getEmail() {
    	return this.email;
    }
    
    public String getUsername() {
    	return this.username;
    }
    
    public String getPassword() {
    	return this.password;
    }
    
    // Setters
    public void setApplicationUserId(Long applicationUserId) {
        this.id = applicationUserId;
    }    
    
    
    public void setEmail(String email) {
    	this.email = email;
    }
    
    public void setUsername(String username) {
    	this.username = username ;
    }
    
    public void setPassword(String password) {
    	this.password = password;
    }
      
}