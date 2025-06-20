package com.javadiscordproject.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.javadiscordproject.domain.model.MessageModel;

@Repository
public interface MessageRepository extends JpaRepository<MessageModel, Long>{
	List<MessageModel> findByConversationId(Long idConversation);
}
