package com.javadiscordproject.presentation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserDTO {
	// Contains fields required for registeration only
	@NotNull @Email
    private String email;
	
	@NotNull
	@Size(min = 4, max = 30)
    private String username;
	
	@NotNull
	@Size(min = 4, max = 40)
    private String password;

    // Empty constructor for JSON deserialization
    public UserDTO() {
    }

    // Constructor with all fields
    public UserDTO(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    // GS
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}