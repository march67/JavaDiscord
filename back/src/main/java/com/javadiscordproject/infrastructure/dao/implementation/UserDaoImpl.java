package com.javadiscordproject.infrastructure.dao.implementation;

import java.util.List;

import com.javadiscordproject.domain.model.UserModel;
import com.javadiscordproject.infrastructure.dao.api.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

@Repository
public class UserDaoImpl implements UserDao {
   @Autowired
   private JdbcTemplate jdbcTemplate;
	   
	@Override
	public UserModel saveUser(UserModel userModel) {
	       String sql = "INSERT INTO application_user (email, username, password) VALUES (?, ?, ?)";
	       jdbcTemplate.update(sql, userModel.getEmail(), userModel.getUsername(), userModel.getPassword());
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
	
}