package com.javadiscordproject.infrastructure.dao.api;

import java.util.List;
import com.javadiscordproject.domain.model.UserModel;

public interface UserDao {
	UserModel saveUser(UserModel userModel);
	UserModel findUserById(String id);
	void deleteUser(String id);
	List<UserModel> findAllUsers();
}