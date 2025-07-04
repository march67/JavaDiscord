package com.javadiscordproject.presentation.dto.message;

import java.time.LocalDateTime;

public class MessagesDto {
	private String username;
	private String messageContent;
	private LocalDateTime messageDateTime;
	
	public MessagesDto(String username, String messageContent, LocalDateTime messageDateTime) {
		this.username = username;
		this.messageContent = messageContent;
		this.messageDateTime = messageDateTime;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

	public LocalDateTime getMessageDateTime() {
		return messageDateTime;
	}

	public void setMessageDateTime(LocalDateTime messageDateTime) {
		this.messageDateTime = messageDateTime;
	}
}
