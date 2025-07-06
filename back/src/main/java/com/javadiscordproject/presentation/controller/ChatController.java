package com.javadiscordproject.presentation.controller;

import com.javadiscordproject.business.service.api.ConversationService;
import com.javadiscordproject.business.service.api.MessageService;
import com.javadiscordproject.business.service.api.UserService;
import com.javadiscordproject.business.service.implementation.MessageServiceImpl;
import com.javadiscordproject.domain.model.ConversationModel;
import com.javadiscordproject.domain.model.MessageModel;
import com.javadiscordproject.infrastructure.security.jwt.CustomUserDetails;
import com.javadiscordproject.presentation.dto.Chat.FriendNotificationDto;
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
    private final ConversationService conversationService;
    private final UserService userService;

    public ChatController(MessageServiceImpl messageService, ConversationService conversationService, UserService userService) {
        this.messageService = messageService;
        this.conversationService = conversationService;
        this.userService = userService;
    }

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public MessageDTO handleMessage(MessageDTO message, SimpMessageHeaderAccessor headerAccessor) {
        return messageService.handleMessage(message, headerAccessor);
    }

    @MessageMapping("/chat/{id}")
    @SendTo("/topic/messages/{id}")
    public MessageDTO handleMessageByConversation(@DestinationVariable String id, MessageDTO message) {
        MessageDTO messageDto = new MessageDTO(
                message.getSender(),
                message.getContent(),
                message.getTimestamp() != null ? message.getTimestamp() :
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                message.getConversationId()        
        );

        Long conversationId = Long.valueOf(message.getConversationId());
              
        MessageModel messageEntity = new MessageModel(
        		userService.findUserByUsername(message.getSender()),
        		conversationService.findConversation(conversationId),
        		message.getContent());
        
        messageService.saveMessage(messageEntity);

        return messageDto;
    }

    @MessageMapping("/notification")
    @SendTo("/topic/notification")
    public FriendNotificationDto handleNotification(FriendNotificationDto friendNotificationDto) {
        return friendNotificationDto;
    }

    @MessageMapping("/notification/friend")
    @SendTo("/topic/notification/friend")
    public String handleFriendNotification(String username) {
        return username;
    }
}