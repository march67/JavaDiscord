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
		findFriendRequest(friendlistModel).ifPresentOrElse(
				entity -> {
					entity.setFriendRequest_status("accepted");
					friendlistRepository.save(entity);
				},
				() -> System.out.println("Friend request not found")
				
			);
	}

	@Override
	public void refuseFriendRequest(FriendlistModel friendlistModel) {
		findFriendRequest(friendlistModel).ifPresentOrElse(
				entity -> {
					entity.setFriendRequest_status("refused");
					friendlistRepository.save(entity);
				},
				() -> System.out.println("Friend request not found")
				
			);
	}

	@Override
	public Optional<FriendlistModel> findFriendRequest(FriendlistModel friendlistModel) {
		FriendlistId id = new FriendlistId(friendlistModel.getUserId_1(), friendlistModel.getUserId_2());
		Optional<FriendlistModel> entity = friendlistRepository.findById(id);
		return entity;
	}

}
