package com.javadiscordproject.presentation.controller;

import com.javadiscordproject.infrastructure.persistence.repository.MessageRepository;
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

    MessageRepository messageRepository;

    public ChatController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public MessageDTO handleMessage(MessageDTO message, SimpMessageHeaderAccessor headerAccessor) {
        Principal user = headerAccessor.getUser();
        if (user != null) {
            message.setSender(user.getName());
        }

        message.setTimestamp(LocalDateTime.now().toString());

        saveMessageToDatabase(message);
        return message;
    }

    private void saveMessageToDatabase(MessageDTO message) {
        // messageRepository.save(message);
        System.out.println("Message : " + message.getSender() + " " + message.getContent() + " " + message.getTimestamp());
    }

    @MessageMapping("/chat/{id}")
    @SendTo("/topic/messages/{id}")
    public MessageDTO handleMessageByConversation(
            @DestinationVariable String id,
            MessageDTO message,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        Principal user = headerAccessor.getUser();
        if (user != null) {
            message.setSender(user.getName());
        }

        message.setTimestamp(LocalDateTime.now().toString());
        message.setConversationId(id);
        saveMessageToDatabase(message);
        return message;
    }

    @MessageMapping("/notification")
    @SendTo("/topic/notifications")
    public NotificationDTO handleNotification(NotificationDTO notification) {
        notification.setTimestamp(LocalDateTime.now().toString());

        System.out.println("Notification: " + notification.getTitle() + " - " + notification.getContent());

        return notification;
    }

}