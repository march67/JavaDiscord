package com.javadiscordproject.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.javadiscordproject.domain.model.UserConversationId;
import com.javadiscordproject.domain.model.UserConversationModel;

@Repository
public interface UserConversationRepository extends JpaRepository<UserConversationModel, UserConversationId> {
	public List<UserConversationModel> findByConversationId(Long conversationId);
	public List<UserConversationModel> findByUserId(Long userId);
	public Long countByConversationId(Long conversationId);
}
