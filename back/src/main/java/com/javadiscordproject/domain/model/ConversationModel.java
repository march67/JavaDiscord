package com.javadiscordproject.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "conversation")
public class ConversationModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String randomFieldToFixBug;
    
    public ConversationModel() {}
    
    public ConversationModel(String randomFieldText) {
    	this.randomFieldToFixBug = randomFieldText;
    }

    public Long getId() {
    	return this.id;
    }

	public String getRandomFieldToFixBug() {
		return randomFieldToFixBug;
	}
} 
