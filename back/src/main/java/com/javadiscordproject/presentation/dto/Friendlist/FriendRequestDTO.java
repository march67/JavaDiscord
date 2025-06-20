package com.javadiscordproject.presentation.dto.Friendlist;

import com.javadiscordproject.domain.model.UserModel;

import jakarta.persistence.Column;

public class FriendRequestDTO {
	private Long userId_1;
	private Long userId_2;	
	
    // Empty constructor for JSON deserialization
    public FriendRequestDTO() {
    }
    
	public FriendRequestDTO(Long userId_1, Long userId_2) {
		this.userId_1 = userId_1;
		this.userId_2 = userId_2;
	}

	public Long getUserId_1() {
		return userId_1;
	}

	public void setUserId_1(Long userId_1) {
		this.userId_1 = userId_1;
	}

	public Long getUserId_2() {
		return userId_2;
	}

	public void setUserId_2(Long userId_2) {
		this.userId_2 = userId_2;
	}
}
