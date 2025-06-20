package com.javadiscordproject.domain.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class UserConversationId implements Serializable{
	@Column(name = "idUser")
	private Long userId;
	
	@Column(name = "idConversation")
	private Long conversationId;
	
	public UserConversationId() {}
	
	public UserConversationId(long userId, long conversationId) {
		this.userId = userId;
		this.conversationId = conversationId;
	}
}
