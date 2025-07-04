package com.javadiscordproject.presentation.controller;

import com.javadiscordproject.business.service.api.MessageService;
import com.javadiscordproject.business.service.implementation.MessageServiceImpl;
import com.javadiscordproject.infrastructure.security.jwt.CustomUserDetails;
import com.javadiscordproject.presentation.dto.Chat.MessageDTO;
import com.javadiscordproject.presentation.dto.Chat.NotificationDTO;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@CrossOrigin(origins = "*")
public class ChatController {

    private final MessageService messageService;

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
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @DestinationVariable String id,
            MessageDTO message,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        String username = customUserDetails != null ? customUserDetails.getUsername() : message.getSender();



        MessageDTO authenticatedMessage = new MessageDTO(
                username,
                message.getContent(),
                message.getTimestamp() != null ? message.getTimestamp() :
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
        );
        //solution PUANTE mais aux moins ça marche
        authenticatedMessage.setSender(message.getSender());
        authenticatedMessage.setConversationId(message.getConversationId());


        System.out.println("Message sender from client: " + message.getSender());
        System.out.println("id: " + message.getConversationId());
        System.out.println("Message content: " + message.getContent());
        System.out.println("Message timestamp: " + authenticatedMessage.getTimestamp());

        return authenticatedMessage;
    }

    @MessageMapping("/notification")
    @SendTo("/topic/notifications")
    public NotificationDTO handleNotification(NotificationDTO notification) {
        return messageService.handleNotification(notification);
    }
}