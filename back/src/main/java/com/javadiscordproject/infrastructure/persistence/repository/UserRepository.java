package com.javadiscordproject.infrastructure.persistence.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.javadiscordproject.domain.model.UserModel;
import com.javadiscordproject.infrastructure.persistence.dao.implementation.UserDaoImpl;

@Repository
public class UserRepository {
    private UserDaoImpl userDaoImpl;
    
    @Autowired
    public UserRepository(UserDaoImpl userDaoImpl) {
        this.userDaoImpl = userDaoImpl;
    }
    
    public void saveUser(UserModel userModel) {
    	userDaoImpl.saveUser(userModel);
    }

	public UserModel findByUsername(String username) {
		return userDaoImpl.findByUsername(username);
	}
}