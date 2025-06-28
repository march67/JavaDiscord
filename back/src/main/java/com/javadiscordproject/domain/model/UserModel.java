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
    
    @Column(unique = true, nullable = false)
    private String email;
    private String username;
    
    @Column(unique = true, nullable = false)
    private String password;
    
    public UserModel() {}
    
    public UserModel(String email, String username, String password) {
    	this.email = email;
    	this.username = username;
    	this.password = password;
    }
    
    public Long getId() {
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