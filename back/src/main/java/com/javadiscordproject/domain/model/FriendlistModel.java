package com.javadiscordproject.domain.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "FriendList")
public class FriendlistModel {
	@EmbeddedId
	private FriendlistId id;
	
	@Column(name = "user_id_1", insertable = false, updatable = false)
	private Long userId1;
	
	@Column(name = "user_id_2", insertable = false, updatable = false)
	private Long userId2;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id_1")
	@MapsId("userId_1")
	private UserModel user_1;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id_2")
	@MapsId("userId_2")
	private UserModel user_2;
	
	private Long requesterId; // used to make sure that the requester doesn't himself accept the request
	
	private String friendRequestStatus;

	public FriendlistModel() { 
		this.id = new FriendlistId();
	}
	 
	public FriendlistModel(FriendlistId friendlistId) {
		this.id = friendlistId;
		this.userId1 = Math.min(friendlistId.getUserId_1(), friendlistId.getUserId_2());
		this.userId2 = Math.max(friendlistId.getUserId_1(), friendlistId.getUserId_2());
	}

	public void setUserId1(Long userId1) {
		this.userId1 = userId1;
	}
	
	public void setUserId2(Long userId2) {
		this.userId2 = userId2;
	}
	
	public String getFriendRequest_status() {
		return friendRequestStatus;
	}

	public void setFriendRequest_status(String friendRequestStatus) {
		this.friendRequestStatus = friendRequestStatus;
	}

	// Handle composite key
	public void setUsers(UserModel user1, UserModel user2) {		
		// handle pair order to avoid duplicata, always lowest as user_1
		Long minId = Math.min(user1.getId(), user2.getId());
		Long maxId = Math.max(user1.getId(), user2.getId());
		if (user1.getId() < user2.getId()) {
			this.user_1 = user1;
			this.user_2 = user2;
		} else {
			this.user_1 = user2;
			this.user_2 = user1;
		}
		
	    // Synchronize IDs between the composite ID and the foreign keys IDs
	    if (user1 != null) {
	        this.userId1 = minId; // foreign key 1
	        this.id.setUserId_1(minId); // composite ID
	    }
	    
	    if (user2 != null) {
	        this.userId2 = maxId; // foreign key 2
	        this.id.setUserId_2(maxId); // composite ID
	    }
	}
	
	public FriendlistId getId() {
		return this.id;
	}
	
	public Long getUserId_1() {
		return this.userId1;
	}
	
	public Long getUserId_2() {
		return this.userId2;
	}
	
	public String getUserName_1() {
		return this.user_1.getUsername();
	}
	
	public String getUserName_2() {
		return this.user_2.getUsername();
	}

	public Long getRequesterId() {
		return requesterId;
	}

	public void setRequesterId(Long requesterId) {
		this.requesterId = requesterId;
	}
	
	public UserModel getUser1() {
		return this.user_1;
	}
	
	public UserModel getUser2() {
		return this.user_2;
	}
}
