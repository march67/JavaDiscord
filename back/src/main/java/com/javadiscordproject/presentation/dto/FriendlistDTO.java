package com.javadiscordproject.presentation.dto;

import jakarta.persistence.Column;

public class FriendlistDTO {
	private Long userId_1;
	
	private Long userId_2;
	
	private String friendRequest_status;
	
    // Empty constructor for JSON deserialization
    public FriendlistDTO() {
    }
    
	public FriendlistDTO(Long userId_1, Long userId_2, String friendRequest_status) {
		this.userId_1 = userId_1;
		this.userId_2 = userId_2;
		this.friendRequest_status = friendRequest_status;
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
