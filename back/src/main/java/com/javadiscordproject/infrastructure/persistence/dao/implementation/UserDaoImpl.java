package com.javadiscordproject.infrastructure.persistence.dao.implementation;

import java.util.List;
import java.util.stream.Stream;

import com.javadiscordproject.domain.model.UserModel;
import com.javadiscordproject.infrastructure.persistence.dao.api.UserDao;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao {
   private JdbcTemplate jdbcTemplate;
   private final PasswordEncoder passwordEncoder;
   
   public UserDaoImpl(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
       this.jdbcTemplate = jdbcTemplate;
       this.passwordEncoder = passwordEncoder;
   }  
	   
	@Override
	public UserModel saveUser(UserModel userModel) {
	       String sql = "INSERT INTO application_user (email, username, password) VALUES (?, ?, ?)";
	       jdbcTemplate.update(sql, userModel.getEmail(), userModel.getUsername(), passwordEncoder.encode(userModel.getPassword()));
	       return userModel;
	}

	@Override
	public UserModel findUserById(String id) {
		return null;
	}

	@Override
	public void deleteUser(String id) {
	}

	@Override
	public List<UserModel> findAllUsers() {
		return null;
	}

	public UserModel findByUsername(String username) {
		String sql = "SELECT username, password FROM application_user WHERE username = ?";
	       try (Stream<UserModel> stream = jdbcTemplate.queryForStream(sql, 
	               (resultSet, rowNum) -> {
	                   UserModel userModel = new UserModel();
	                   userModel.setUsername(resultSet.getString("username"));
	                   userModel.setPassword(resultSet.getString("password"));
	                   return userModel;
	               },
	               username)) {
	       	return stream.findFirst()
	       			.orElseThrow( () -> new UsernameNotFoundException("User not found"));
	       }
	}
	
}