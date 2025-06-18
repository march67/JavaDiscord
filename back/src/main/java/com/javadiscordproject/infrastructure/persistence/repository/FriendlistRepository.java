package com.javadiscordproject.infrastructure.persistence.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.javadiscordproject.domain.model.FriendlistId;
import com.javadiscordproject.domain.model.FriendlistModel;

@Repository
public interface FriendlistRepository extends JpaRepository<FriendlistModel, FriendlistId>{
	// concate both for whole list
	List<FriendlistModel> findByUserId1AndFriendRequestStatus(Long user_id_1, String friendRequestStatus); // column 1
	List<FriendlistModel> findByUserId2AndFriendRequestStatus(Long user_id_1, String friendRequestStatus); // column 2
	
}
