package com.javadiscordproject.infrastructure.security.jwt;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {
	// this class allows to customize the claims in the token, implements UserDetails which is an interface from the framework
	private Long id;
	private String username;
	private String password;
	private String role; // not handled yet, not necessary yet ?
	
	public CustomUserDetails(Long id, String username, String password, String role) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.role = role;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		//return List.of(new SimpleGrantedAuthority("ROLE_" + role));
		return Arrays.asList(new SimpleGrantedAuthority("ROLE_" + role));
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	public Long getId() {
		return id;
	}

}
