package com.javadiscordproject.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "conversation")
public class ConversationModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @CreationTimestamp
    private LocalDateTime lastMessageDateTime;
    
    public ConversationModel() {}
    
    public ConversationModel(Long id) {
    	this.id = id;
    }
    
    public ConversationModel(Long id, LocalDateTime lastMessageDateTime) {
    	this.lastMessageDateTime = lastMessageDateTime;
    	this.id = id;
    }
    
    public Long getId() {
    	return this.id;
    }
    
	public LocalDateTime getLastMessageDateTime() {
		return lastMessageDateTime;
	}

	public void setLastMessageDateTime(LocalDateTime lastMessageDateTime) {
		this.lastMessageDateTime = lastMessageDateTime;
	}
} 
