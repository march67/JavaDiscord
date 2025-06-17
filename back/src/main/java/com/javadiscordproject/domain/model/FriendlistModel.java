package com.javadiscordproject.domain.model;

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
	
	private String friendRequest_status;
	
	public FriendlistModel() { 
		this.id = new FriendlistId();
	}

	public void setUserId1(Long userId1) {
		this.userId1 = userId1;
	}
	
	public void setUserId2(Long userId2) {
		this.userId2 = userId2;
	}
	
	public String getFriendRequest_status() {
		return friendRequest_status;
	}

	public void setFriendRequest_status(String friendRequest_status) {
		this.friendRequest_status = friendRequest_status;
	}

	public void setUsers(UserModel user1, UserModel user2) {
		this.user_1 = user1;
		this.user_2 = user2;
		
	    // Mettre à jour les IDs dans l'objet composite
	    if (user1 != null) {
	        this.userId1 = user1.getId();
	        this.id.setUserId_1(user1.getId());
	    }
	    
	    if (user2 != null) {
	        this.userId2 = user2.getId();
	        this.id.setUserId_2(user2.getId());
	    }
	}
}
