package com.javadiscordproject.infrastructure.configuration;

import com.javadiscordproject.business.service.implementation.CustomUserDetailsService;
import com.javadiscordproject.business.service.ws.JwtAuthenticationService;
import com.javadiscordproject.infrastructure.interceptor.JwtHandshakeInterceptor;
import com.javadiscordproject.infrastructure.security.jwt.JwtTokenUtil;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebsocketConfiguration implements WebSocketMessageBrokerConfigurer {

    private final JwtAuthenticationService jwtAuthenticationService;
    private CustomUserDetailsService customUserDetailsService;
    private JwtTokenUtil jwtTokenUtil;



    public WebsocketConfiguration(JwtAuthenticationService jwtAuthenticationService, CustomUserDetailsService customUserDetailsService, JwtTokenUtil jwtTokenUtil) {
        this.jwtAuthenticationService = jwtAuthenticationService;
        this.customUserDetailsService = customUserDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }


    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .addInterceptors(new JwtHandshakeInterceptor());
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String authToken = accessor.getFirstNativeHeader("Authorization");

                    if (authToken != null && authToken.startsWith("Bearer ")) {
                        String token = authToken.substring(7);

                        try {
                            String username = jwtAuthenticationService.getUsernameFromToken(token);
                            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
                            if (jwtTokenUtil.validateToken(token, userDetails)) {
                                UsernamePasswordAuthenticationToken authentication =
                                        new UsernamePasswordAuthenticationToken(
                                                userDetails, null, userDetails.getAuthorities()
                                        );
                                accessor.setUser(authentication);
                                SecurityContextHolder.getContext().setAuthentication(authentication);
                            } else {
                                throw new SecurityException("Invalid JWT token");
                            }
                        } catch (Exception e) {
                            throw new SecurityException("Authentication failed: " + e.getMessage());
                        }
                    } else {
                        throw new SecurityException("Missing or invalid Authorization header");
                    }
                }

                return message;
            }
        });
    }
}