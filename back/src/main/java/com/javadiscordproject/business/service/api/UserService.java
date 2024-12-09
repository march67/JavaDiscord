package com.javadiscordproject.business.service.api;

import com.javadiscordproject.domain.model.UserModel;
import com.javadiscordproject.presentation.dto.UserDTO;

public interface UserService {

	UserModel createUser(UserDTO userDTO) {
		return userDTO;
	}
	
}