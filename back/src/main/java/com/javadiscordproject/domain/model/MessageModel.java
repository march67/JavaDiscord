package com.javadiscordproject.domain.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "message")
public class MessageModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	public void setId(Long id) {
		this.id = id;
	}

	public void setUser(UserModel user) {
		this.user = user;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	@ManyToOne
    @JoinColumn(name = "userId")
	private UserModel user;

	public void setConversation(ConversationModel conversation) {
		this.conversation = conversation;
	}

	@ManyToOne
    @JoinColumn(name = "conversationId")
    private ConversationModel conversation;
    
    private String messageContent;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    public MessageModel() {
    }
    
    public MessageModel(UserModel messageAuthor, ConversationModel conversation, String messageContent) {
    	this.user = messageAuthor;
    	this.conversation = conversation;
    	this.messageContent = messageContent;
    }

	public Long getId() {
		return this.id;
	}
	
	public ConversationModel getConversation() {
		return this.conversation;
	}
	
	public UserModel getUser() {
		return this.user;
	}

	public String getMessageContent() {
		return messageContent;
	}
	
	public LocalDateTime getCreatedAt() {
		return this.createdAt;
	}
}
