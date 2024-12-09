package com.javadiscordproject.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

@Entity
@Table(name = "application_user")
public class UserModel {
    private String application_user_id;
    private String email;
    private String username;
    private String password;
    
    public String getApplicationUserId() {
    	return this.application_user_id;
    }
    
    public String getEmail() {
    	return this.email;
    }
    
    public String getUserName() {
    	return this.username;
    }
    
    public String getPassword() {
    	return this.password;
    }
}