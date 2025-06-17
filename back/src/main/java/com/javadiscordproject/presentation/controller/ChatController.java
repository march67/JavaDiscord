package com.javadiscordproject.presentation.controller;

import com.javadiscordproject.domain.model.ChatMessage;
import com.javadiscordproject.infrastructure.persistence.repository.MessageRepository;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
@CrossOrigin(origins = "*")
public class ChatController {

    MessageRepository messageRepository;

    public ChatController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ChatMessage handleMessage(ChatMessage message, SimpMessageHeaderAccessor headerAccessor) {
        // Get authenticated user from WebSocket session
        Principal user = headerAccessor.getUser();
        if (user != null) {
            // Set the sender from the authenticated user
            message.setSender(user.getName());
        }

        // Set timestamp
        message.setTimestamp(LocalDateTime.now().toString());

        saveMessageToDatabase(message);
        return message;
    }

    private void saveMessageToDatabase(ChatMessage message) {
        // messageRepository.save(message);
        System.out.println("Message : " + message.getSender() + " " + message.getContent() + " " + message.getTimestamp());
    }
}