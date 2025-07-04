package com.javadiscordproject.business.service.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.javadiscordproject.domain.model.UserModel;
import com.javadiscordproject.infrastructure.persistence.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private PasswordEncoder passwordEncoder;
	
	@InjectMocks
	private UserServiceImpl userService;
	
	@Test
	void findUserByUsername_shouldReturnUser_whenUserExists() {
		// Arrange
		String username = "user";
		UserModel user = new UserModel();
		user.setUsername(username);
		
		when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
		
		// Act
		UserModel result = userService.findUserByUsername(username);
		
		// Assert
		assertNotNull(result);
		assertEquals(username, result.getUsername());
		verify(userRepository).findByUsername(username);
	}
	
	@Test
	void findUserByUsername_shouldThrowEntityNotFoundException_whenUserDoesNotExist() {
		// Arrange
		String username = "userNotExist";
		UserModel user = new UserModel();
		user.setUsername(username);
		
		when(userRepository.findByUsername(username)).thenThrow(new EntityNotFoundException());
		
		// Act & Assert
		assertThrows(
				EntityNotFoundException.class,
				() -> {userService.findUserByUsername(username);}
		);
		verify(userRepository).findByUsername(username);
	}
}
