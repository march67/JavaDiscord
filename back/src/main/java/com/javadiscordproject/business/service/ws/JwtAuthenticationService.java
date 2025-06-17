package com.javadiscordproject.business.service.ws;

import com.javadiscordproject.infrastructure.security.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class JwtAuthenticationService {

    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public JwtAuthenticationService(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public String generateToken(String username) {
        // Ici, on crée un UserDetails minimal pour la génération du token
        User userDetails = new User(username, "", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        return jwtTokenUtil.generateToken(userDetails);
    }

    public String getUsernameFromToken(String token) {
        return jwtTokenUtil.extractUsername(token);
    }

    public boolean isTokenValid(String token) {
        // Ici, on ne peut pas valider sans UserDetails, donc on vérifie juste l'expiration
        return !jwtTokenUtil.extractExpiration(token).before(new java.util.Date());
    }

    public UsernamePasswordAuthenticationToken validateTokenAndGetAuthentication(String token) {
        String username = getUsernameFromToken(token);
        if (username != null && isTokenValid(token)) {
            List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                    new SimpleGrantedAuthority("ROLE_USER")
            );
            return new UsernamePasswordAuthenticationToken(username, null, authorities);
        }
        return null;
    }
}
