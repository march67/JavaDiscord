package com.javadiscordproject.domain.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "UserConversation")
public class UserConversationModel {
	@EmbeddedId
	private UserConversationId id;
	
	@ManyToOne
	@JoinColumn
	@MapsId("userId")
	private UserModel user;
	
	@ManyToOne
	@JoinColumn
	@MapsId("conversationId")
	private ConversationModel conversation;
	
	@CreationTimestamp
	private LocalDateTime joined_at;
	
	@UpdateTimestamp
    private LocalDateTime lastMessageDateTime;
	
	public UserConversationModel() {
	}
	
	public UserConversationModel(UserModel user, ConversationModel conversation) {
		this.user = user;
		this.conversation = conversation;
		
		this.id = new UserConversationId(user.getId(), conversation.getId());
	}
	
	public LocalDateTime getLastMessageDateTime() {
		return lastMessageDateTime;
	}
	
	public void updateLastMessageDateTime() {
		this.lastMessageDateTime = LocalDateTime.now();
	}
	
	public UserConversationId getId() {
		return this.id;
	}
}
