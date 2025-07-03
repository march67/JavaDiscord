package com.javadiscordproject.managers;

import com.javadiscordproject.models.UserSession;

public class SessionManager {
    private static SessionManager instance;
    private UserSession currentSession;

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void createSession(String username, String token) {
        this.currentSession = new UserSession(username, token);
        System.out.println("Session created for user: " + username + " with token: " + token);
    }

    public UserSession getCurrentSession() {
        return currentSession;
    }

    public boolean isAuthenticated() {
        return currentSession != null && currentSession.getToken() != null;
    }

    public void clearSession() {
        this.currentSession = null;
    }

    public String getUsername() {
        return isAuthenticated() ? currentSession.getUsername() : null;
    }

    public String getToken() {
        return isAuthenticated() ? currentSession.getToken() : null;
    }
}