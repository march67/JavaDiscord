package com.javadiscordproject.business.service.api;

import com.javadiscordproject.domain.model.UserModel;
import com.javadiscordproject.infrastructure.persistence.repository.UserRepository;
import com.javadiscordproject.presentation.dto.UserDTO;


public interface UserService {
	public void registerUser(UserModel userModel);
	public UserModel findByUsername(String username);
}