package com.javadiscordproject.presentation.controller;

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

    private MessageRepository messageRepository;

    private UserRepository userRepository;

    private ConversationRepository conversationRepository;

    public ChatController(MessageRepository messageRepository, UserRepository userRepository, ConversationRepository conversationRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.conversationRepository = conversationRepository;
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

    private void saveMessageToDatabase(MessageDTO messageDTO) {
        MessageModel message = new MessageModel();

        UserModel user = userRepository.findByUsername(messageDTO.getSender());

        ConversationModel conversation = conversationRepository.findById(Long.parseLong(messageDTO.getConversationId()))
                .orElseThrow(() -> new RuntimeException("Conversation non trouvée"));

        message.setUser(user);
        message.setConversation(conversation);
        message.setMessageContent(messageDTO.getContent());

        if (messageDTO.getTimestamp() != null) {
            message.setCreatedAt(LocalDateTime.parse(messageDTO.getTimestamp()));
        }

        messageRepository.save(message);
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