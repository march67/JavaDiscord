package com.javadiscordproject.domain.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "FriendList")
public class FriendlistModel {
	@EmbeddedId
	private FriendlistId id;
	
	@ManyToOne
	@MapsId("userId_1")
	private UserModel user_1;
	
	@ManyToOne
	@MapsId("userId_2")
	private UserModel user_2;
	
	private String friendRequest_status;
}
