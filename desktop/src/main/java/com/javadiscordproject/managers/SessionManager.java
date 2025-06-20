package com.javadiscordproject.managers;

import com.javadiscordproject.models.UserSession;

public class SessionManager {
    private static SessionManager instance;
    private UserSession currentSession;
    private String token;

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void createSession(String username, String email) {
        this.currentSession = new UserSession(username, email);
    }

    public UserSession getCurrentSession() {
        return currentSession;
    }

    public boolean isAuthenticated() {
        return currentSession != null;
    }

    public void clearSession() {
        this.currentSession = null;
    }

    public String getUsername() {
        return isAuthenticated() ? currentSession.getUsername() : null;
    }

    public String getEmail() {
        return isAuthenticated() ? currentSession.getEmail() : null;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}