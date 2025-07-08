package com.javadiscordproject.infrastructure.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {

        String jwt = null;

        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpServletRequest httpRequest = servletRequest.getServletRequest();

            // 1) Authorization Header (Desktop)
            String authHeader = httpRequest.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                jwt = authHeader.substring(7);
                System.out.println("JWT found in Authorization header");
            }

            // 2) URL Parameter (Web)
            if ((jwt == null || jwt.isEmpty()) && httpRequest.getParameter("token") != null) {
                jwt = httpRequest.getParameter("token");
                System.out.println("JWT found in URL parameter");
            }
        }

        if (jwt != null && !jwt.isEmpty()) {
            attributes.put("jwt", jwt);
            System.out.println("WebSocket handshake successful with JWT");
            return true;
        } else {
            System.out.println("Missing JWT token in both Authorization header and URL parameters - handshake rejected");
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Exception exception) {
        if (exception != null) {
            System.err.println("WebSocket handshake failed: " + exception.getMessage());
        }
    }
}
