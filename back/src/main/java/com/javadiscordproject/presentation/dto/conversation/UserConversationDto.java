package com.javadiscordproject.presentation.dto.conversation;

import java.time.LocalDateTime;

public class UserConversationDto {
	private Long conversationId;
	private LocalDateTime lastMessageDateTime;
	
	public UserConversationDto(Long conversationId, LocalDateTime lastMessageDateTime) {
		this.conversationId = conversationId;
		this.lastMessageDateTime = lastMessageDateTime;
	}

	public Long getConversationId() {
		return conversationId;
	}

	public void setConversationId(Long conversationId) {
		this.conversationId = conversationId;
	}

	public LocalDateTime getLastMessageDateTime() {
		return lastMessageDateTime;
	}

	public void setLastMessageDateTime(LocalDateTime lastMessageDateTime) {
		this.lastMessageDateTime = lastMessageDateTime;
	}
}
