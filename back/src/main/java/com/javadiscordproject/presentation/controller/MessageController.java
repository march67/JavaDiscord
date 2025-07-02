package com.javadiscordproject.presentation.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javadiscordproject.business.service.api.MessageService;
import com.javadiscordproject.presentation.dto.message.MessagesDto;

@RestController
@RequestMapping("/message")
public class MessageController {
	MessageService messageService;
	
	public MessageController(MessageService messageService) {
		this.messageService = messageService;
	}
	
	@GetMapping("/{conversationId}")
	public ResponseEntity<List<MessagesDto>> getMessagesByConversationId(@PathVariable Long conversationId) {
		return ResponseEntity.ok(messageService.getMessagesByConversationId(conversationId).stream()
				.map(m -> new MessagesDto(
						m.getUser().getUsername(),
						m.getMessageContent(),
						m.getCreatedAt()
						))
				.collect(Collectors.toList()));
	}
	
}
