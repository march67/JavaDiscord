package com.javadiscordproject.business.service.implementation;

import org.springframework.stereotype.Component;

import com.javadiscordproject.domain.model.UserModel;
import com.javadiscordproject.presentation.dto.UserDTO;

@Component
public class UserMapper {
    public UserModel toModel(UserDTO userDTO) {
        UserModel userModel = new UserModel();
        userModel.setEmail(userDTO.getEmail());
        userModel.setUsername(userDTO.getUsername());
        userModel.setPassword(userDTO.getPassword());
        return userModel;
    }

    public UserDTO toDTO(UserModel userModel) {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(userModel.getEmail());
        userDTO.setUsername(userModel.getUsername());
        userDTO.setPassword(userModel.getPassword());
        return userDTO;
    }
}