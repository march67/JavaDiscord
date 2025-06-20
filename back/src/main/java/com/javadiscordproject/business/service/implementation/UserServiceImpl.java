package com.javadiscordproject.business.service.implementation;

import com.javadiscordproject.business.service.api.UserService;
import com.javadiscordproject.domain.model.UserModel;
import com.javadiscordproject.infrastructure.persistence.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
	private UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserServiceImpl (UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	public void registerUser(UserModel userModel) {
		userModel.setPassword(passwordEncoder.encode(userModel.getPassword())); // crypt the password before saving in the DB => allow jwt and security config to work properly 
		userRepository.save(userModel);
	}

	public UserModel getUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public String getUsernameById(Long userId) {
		return userRepository.findById(userId).map(UserModel::getUsername).orElse(null);
	}
}