package com.javadiscordproject.models;

public class UserDTO {
    private String email;
    private String username;
    private String password;

    // Constructeur complet
    public UserDTO(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    // Constructeur avec uniquement username (pour la rétrocompatibilité)
    public UserDTO(String username) {
        this.username = username;
    }

    // Getters et Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}