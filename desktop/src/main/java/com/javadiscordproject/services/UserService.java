// src/main/java/com/javadiscordproject/services/UserService.java
package com.javadiscordproject.services;

import com.javadiscordproject.models.UserDTO;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UserService {
    public List<UserDTO> getUsers(String filter) {
        String username = filter.toLowerCase();
        return getMockUsers().stream()
                .filter(user -> containsAllChars(user.username().toLowerCase(), username))
                .collect(Collectors.toList());
    }

    private boolean containsAllChars(String username, String filter) {
        for (char c : filter.toCharArray()) {
            int count = countChar(username, c);
            int required = countChar(filter, c);
            if (count < required) return false;
        }
        return true;
    }

    private int countChar(String str, char c) {
        return (int) str.chars().filter(ch -> ch == c).count();
    }

    private List<UserDTO> getMockUsers() {
        return Arrays.asList(
                new UserDTO("User 1"),
                new UserDTO("User 2"),
                new UserDTO("User 3"),
                new UserDTO("User 4"),
                new UserDTO("User 5")
        );
    }
}