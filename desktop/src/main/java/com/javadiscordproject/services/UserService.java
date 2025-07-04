package com.javadiscordproject.services;

import com.javadiscordproject.models.UserDTO;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UserService {
    public List<UserDTO> getUsers(String filter) {
        String username = filter.toLowerCase();
        return getMockUsers().stream()
                .filter(user -> containsAllChars(user.getUsername().toLowerCase(), username))
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
                new UserDTO(null, "User 1", null),
                new UserDTO(null, "User 2", null),
                new UserDTO(null, "User 3", null),
                new UserDTO(null, "User 4", null),
                new UserDTO(null, "User 5", null)
        );
    }
}