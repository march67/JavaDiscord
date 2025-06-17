package com.javadiscordproject.domain.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class FriendlistId implements Serializable{
	@Column(name = "idUser_1")
	private Long userId_1;
	
	@Column(name = "idUser_2")
	private Long userId_2;
	
	public FriendlistId() {}
	
	public FriendlistId(long userId_1, long userId_2) {
		this.userId_1 = userId_1;
		this.userId_1 = userId_2;
	}
}
