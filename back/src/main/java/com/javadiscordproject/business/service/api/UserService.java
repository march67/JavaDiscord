package com.javadiscordproject.business.service.api;

import com.javadiscordproject.domain.model.UserModel;
import com.javadiscordproject.infrastructure.persistence.repository.UserRepository;
import com.javadiscordproject.presentation.dto.User.UserDTO;


public interface UserService {
	public void saveUser(UserModel userModel);
	public UserModel findUserByUsername(String username);
	public UserModel findUserById(Long id);
	public String getUsernameById(Long userId);
}