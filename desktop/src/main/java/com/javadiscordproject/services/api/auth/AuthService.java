// AuthService.java
package com.javadiscordproject.services.api.auth;

import com.javadiscordproject.models.RegisterDTO;
import com.javadiscordproject.models.LoginDTO;
import com.javadiscordproject.utils.ApiConstants;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.concurrent.CompletableFuture;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AuthService implements IAuthService {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public AuthService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public CompletableFuture<Boolean> register(RegisterDTO registerDto) {
        try {
            String jsonBody = objectMapper.writeValueAsString(registerDto);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ApiConstants.USERS_ENDPOINT))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> {
                        int status = response.statusCode();
                        return status == 201 || status == 200;
                    });
        } catch (Exception e) {
            return CompletableFuture.completedFuture(false);
        }
    }

    @Override
    public CompletableFuture<Boolean> login(LoginDTO loginDto) {
        try {
            String jsonBody = objectMapper.writeValueAsString(loginDto);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ApiConstants.AUTHENTICATE_ENDPOINT))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> {
                        int status = response.statusCode();
                        return status == 200;
                    });
        } catch (Exception e) {
            return CompletableFuture.completedFuture(false);
        }
    }
}