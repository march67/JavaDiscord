package com.javadiscordproject.infrastructure.persistence.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.stream.Stream;

import com.javadiscordproject.domain.model.UserModel;
import com.javadiscordproject.infrastructure.persistence.dao.implementation.UserDaoImpl;

@ExtendWith(MockitoExtension.class)
public class UserDaoImplTest {
	
    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private PasswordEncoder passwordEncoder;
    
    @Mock
    private UserModel userModel;

    @InjectMocks
    private UserDaoImpl userDaoImpl;
    
    @Test
    public void testSaveUser_ShouldSaveUserAndEncodePasswordAndReturnUserModel() {
    	// Arrange
    	UserModel userModel = new UserModel();
    	userModel.setEmail("userTestgmailcom");
    	userModel.setPassword("passwordUserTest");
    	userModel.setUsername("userUsernameTest");
    	
    	String expectedSql = "INSERT INTO application_user (email, username, password) VALUES (?, ?, ?)";
    	
    	String encodedPassword = "encodedPasswordTest";
    	
    	when(passwordEncoder.encode(userModel.getPassword())).thenReturn(encodedPassword);
    	
    	when(jdbcTemplate.update(
    			eq(expectedSql),
    			eq(userModel.getEmail()),
    			eq(userModel.getUsername()),
    			eq(encodedPassword)
    			)).thenReturn(1); // successful insertion with 1, fail with 0
    	
    	// Act
    	UserModel result = userDaoImpl.saveUser(userModel);
    	
    	// Assert
    	assertNotNull(result);
    	assertEquals(userModel, result); // verify that UserModel is returned
    	
    	verify(jdbcTemplate).update(
    			eq(expectedSql),
    			eq(userModel.getEmail()),
    			eq(userModel.getUsername()),
    			eq(encodedPassword)); // verfiy that update calls with the encoded password
    	
    	verify(passwordEncoder).encode(userModel.getPassword()); // verify that the password of the UserModel is encoded
    }
    
    @Test
    public void testFindByUsername_ShouldReturnUserModel() {
    	// Arrange
    	String username = "usernameTest";
    	String expectedSql = "SELECT username, password FROM application_user WHERE username = ?";
    	
    	UserModel expectedUser = new UserModel();
    	expectedUser.setUsername(username);
    	expectedUser.setPassword("passwordTest");
    	
    	when(jdbcTemplate.<UserModel>queryForStream(
    		    eq(expectedSql),
    		    ArgumentMatchers.<RowMapper<UserModel>>any(),
    		    eq(username)
    		)).thenReturn(Stream.of(expectedUser));
    	
    	// Act
    	UserModel result = userDaoImpl.findByUsername(username);
    	
    	// Assert
    	assertNotNull(result);
    	assertEquals(result, expectedUser);
    	
    	verify(jdbcTemplate).queryForStream(
    		    eq(expectedSql),
    		    ArgumentMatchers.<RowMapper<UserModel>>any(),
    		    eq(username)
    		); // verify that the sql request is correct + the username to find is used + that the method is called
    }
}