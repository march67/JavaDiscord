package com.javadiscordproject.domain.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "UserConversation")
public class UserConversationModel {
	@EmbeddedId
	private UserConversationId id;
	
	@ManyToOne
	@MapsId("userId")
	private UserModel user;
	
	@ManyToOne
	@MapsId("conversationId")
	private ConversationModel conversation;
	
	@CreationTimestamp
	private LocalDateTime joined_at;
}
