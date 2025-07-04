package com.javadiscordproject.presentation.dto.Chat;

public class FriendNotificationDto {
	private String username_requester;
	private String username_receiver;
	
	public String getUsername_requester() {
		return username_requester;
	}
	public void setUsername_requester(String username_requester) {
		this.username_requester = username_requester;
	}
	public String getUsername_receiver() {
		return username_receiver;
	}
	public void setUsername_receiver(String username_receiver) {
		this.username_receiver = username_receiver;
	}
	
	public FriendNotificationDto(String username_requester, String username_receiver) {
		super();
		this.username_requester = username_requester;
		this.username_receiver = username_receiver;
	}
	
	public FriendNotificationDto() {}
	
}
