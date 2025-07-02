package com.javadiscordproject.presentation.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javadiscordproject.business.service.api.ConversationService;
import com.javadiscordproject.business.service.api.UserConversationService;
import com.javadiscordproject.domain.model.UserConversationModel;
import com.javadiscordproject.infrastructure.security.jwt.CustomUserDetails;
import com.javadiscordproject.presentation.dto.conversation.UserConversationDto;

@RestController
@RequestMapping("/conversation")
public class ConversationController {
	ConversationService conversationService;
	UserConversationService userConversationService;
	
	public ConversationController(ConversationService conversationService, UserConversationService userConversationService) {
		this.conversationService = conversationService;
		this.userConversationService = userConversationService;
	}
	
	@GetMapping()
	public ResponseEntity<List<UserConversationDto>> getConversations(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		Long userId = customUserDetails.getId();
		List<UserConversationModel> userConversations = userConversationService.findByUserId(userId);
		
		List<UserConversationDto> response = userConversations.stream()
				.map(c -> new UserConversationDto(
						c.getId().getConversationId(),
						c.getLastMessageDateTime()
						))
				.collect(Collectors.toList());
		
		return ResponseEntity.ok(response);
	}
}
