package com.javadiscordproject.presentation.controller;

import com.javadiscordproject.domain.model.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@CrossOrigin(origins = "*")
public class ChatController {

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ChatMessage handleMessage(ChatMessage message) {
        saveMessageToDatabase(message);
        return message;
    }

    private void saveMessageToDatabase(ChatMessage message) {
        //TODO : write function
        System.out.println("Message : " + message.getSender() + " " + message.getContent() + " " + message.getTimestamp());
    }
}