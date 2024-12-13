package com.javadiscordproject.services.api;

import com.javadiscordproject.models.UserDTO;

import java.util.concurrent.CompletableFuture;

public interface IAuthService {
    CompletableFuture<Boolean> register(UserDTO userDto);
    CompletableFuture<Boolean> login(String username, String password);
}
