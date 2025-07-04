// IAuthService.java
package com.javadiscordproject.services.api.auth;

import java.util.concurrent.CompletableFuture;
import com.javadiscordproject.models.RegisterDTO;
import com.javadiscordproject.models.LoginDTO;

public interface IAuthService {
    CompletableFuture<Boolean> register(RegisterDTO registerDto);
    CompletableFuture<String> login(LoginDTO loginDto);
}