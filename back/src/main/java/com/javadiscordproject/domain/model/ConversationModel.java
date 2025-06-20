package com.javadiscordproject.domain.model;

import java.util.ArrayList;
import java.util.List;

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
    
    public ConversationModel() {}
    
    public ConversationModel(Long id) {
    	this.id = id;
    }
    
    public Long getId() {
    	return this.id;
    }
} 
