package com.javadiscordproject.mapper;

import org.springframework.stereotype.Component;

import com.javadiscordproject.domain.model.FriendlistId;
import com.javadiscordproject.domain.model.FriendlistModel;
import com.javadiscordproject.domain.model.UserModel;
import com.javadiscordproject.infrastructure.persistence.repository.UserRepository;
import com.javadiscordproject.presentation.dto.Friendlist.FriendRequestByIdDTO;

@Component
public class FriendlistMapper {
	private final UserRepository userRepository;
	
	public FriendlistMapper(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
    public FriendlistModel toModel(FriendRequestByIdDTO friendRequestByIdDTO) {
    	FriendlistModel friendlistModel = new FriendlistModel();
    	
    	// Search for the two existing users
    	UserModel user1 = userRepository.findById(friendRequestByIdDTO.getUserId_1()).orElseThrow(() -> new RuntimeException("Utilisateur 1 non trouvé"));
    	UserModel user2 = userRepository.findById(friendRequestByIdDTO.getUserId_2()).orElseThrow(() -> new RuntimeException("Utilisateur 2 non trouvé"));
    	
        friendlistModel.setUsers(user1, user2);
        
        return friendlistModel;
    }

    public FriendRequestByIdDTO toDTO(FriendlistModel friendlistModel) { 
        FriendRequestByIdDTO friendRequestByIdDTO = new FriendRequestByIdDTO();
        
        friendRequestByIdDTO.setUserId_1(friendRequestByIdDTO.getUserId_1());
        friendRequestByIdDTO.setUserId_2(friendRequestByIdDTO.getUserId_2());
        
        return friendRequestByIdDTO;
    }
}
