package com.javadiscordproject.infrastructure.repository;

import org.springframework.stereotype.Repository;

import com.javadiscordproject.domain.model.UserModel;
import com.javadiscordproject.infrastructure.dao.implementation.UserDaoImpl;

@Repository
public class UserRepository {
    private UserDaoImpl userDaoImpl;
    
    public UserRepository(UserDaoImpl userDaoImpl) {
        this.userDaoImpl = userDaoImpl;
    }
    
    public void saveUser(UserModel userModel) {
    	userDaoImpl.saveUser(userModel);
    }
}