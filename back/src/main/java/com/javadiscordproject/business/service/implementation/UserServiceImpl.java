package com.javadiscordproject.business.service.implementation;

import com.javadiscordproject.business.service.api.UserService;
import com.javadiscordproject.domain.model.UserModel;
import com.javadiscordproject.infrastructure.repository.UserRepository;

import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
	private UserRepository userRepository;

	public UserServiceImpl (UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public void registerUser(UserModel userModel) {
		userRepository.saveUser(userModel);
	}
}