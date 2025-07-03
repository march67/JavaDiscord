package com.javadiscordproject.presentation.controller;

import com.javadiscordproject.business.service.implementation.MessageServiceImpl;
import com.javadiscordproject.domain.model.ConversationModel;
import com.javadiscordproject.domain.model.MessageModel;
import com.javadiscordproject.domain.model.UserModel;
import com.javadiscordproject.infrastructure.persistence.repository.ConversationRepository;
import com.javadiscordproject.infrastructure.persistence.repository.MessageRepository;
import com.javadiscordproject.infrastructure.persistence.repository.UserRepository;
import com.javadiscordproject.presentation.dto.Chat.MessageDTO;
import com.javadiscordproject.presentation.dto.Chat.NotificationDTO;
import org.springframework.messaging.handler.annotation.DestinationVariable;
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

    private MessageServiceImpl messageService;

    public ChatController(MessageServiceImpl messageService) {
        this.messageService = messageService;
    }

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public MessageDTO handleMessage(MessageDTO message, SimpMessageHeaderAccessor headerAccessor) {
        return messageService.handleMessage(message, headerAccessor);
    }

    @MessageMapping("/chat/{id}")
    @SendTo("/topic/messages/{id}")
    public MessageDTO handleMessageByConversation(
            @DestinationVariable String id,
            MessageDTO message,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        return messageService.handleMessageByConversation(id, message, headerAccessor);
    }

    @MessageMapping("/notification")
    @SendTo("/topic/notifications")
    public NotificationDTO handleNotification(NotificationDTO notification) {
        return messageService.handleNotification(notification);
    }

}