package com.javadiscordproject.business.service.implementation;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.javadiscordproject.business.service.api.FriendlistService;
import com.javadiscordproject.domain.model.FriendlistId;
import com.javadiscordproject.domain.model.FriendlistModel;
import com.javadiscordproject.infrastructure.persistence.repository.FriendlistRepository;

@Service
public class FriendlistServiceImpl implements FriendlistService {
	private FriendlistRepository friendlistRepository;
	
	public FriendlistServiceImpl(FriendlistRepository friendlistRepository) {
		this.friendlistRepository = friendlistRepository;
	}

	@Override
	public void sendFriendRequest(FriendlistModel friendlistModel) {
		friendlistModel.setFriendRequest_status("pending");
		friendlistRepository.save(friendlistModel);
	}

	@Override
	public void acceptFriendRequest(FriendlistModel friendlistModel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refuseFriendRequest(FriendlistModel friendlistModel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void findFriendRequest(FriendlistModel friendlistModel) {
		FriendlistId id = new FriendlistId(friendlistModel.getUserId_1(), friendlistModel.getUserId_2());
		Optional<FriendlistModel> result = friendlistRepository.findById(id);
		System.out.println(result);
	}

}
