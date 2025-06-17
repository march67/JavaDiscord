package com.javadiscordproject.business.service.implementation;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.javadiscordproject.domain.model.UserModel;
import com.javadiscordproject.infrastructure.security.jwt.CustomUserDetails;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserServiceImpl userServiceImpl;

    public CustomUserDetailsService(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel user = userServiceImpl.findByUsername(username);
        
        // old without the CustomUserDetails
//        return org.springframework.security.core.userdetails.User
//                .withUsername(user.getUsername())
//                .password(user.getPassword())
//                .roles("USER")
//                .build();
        
        // new with CustomUserDetails ( to put in the user ID in the claims )
        return new CustomUserDetails(
        		user.getId(),
        		user.getUsername(),
        		user.getPassword(),
        		"USER"
        		);
    }
}