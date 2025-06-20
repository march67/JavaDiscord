package com.javadiscordproject;

import java.time.LocalDateTime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.javadiscordproject.business.service.api.ConversationService;
import com.javadiscordproject.business.service.api.MessageService;
import com.javadiscordproject.business.service.api.UserService;
import com.javadiscordproject.domain.model.ConversationModel;
import com.javadiscordproject.domain.model.MessageModel;
import com.javadiscordproject.domain.model.UserModel;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        
        // Test create conversation
    	
        // Retrieve spring context
        ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);
        ConversationService conversationService = context.getBean(ConversationService.class);
        UserService userService = context.getBean(UserService.class);
        MessageService messageService = context.getBean(MessageService.class);
        
        // Creating users
        UserModel user1 = new UserModel("test1@gmail.com", "test", "test");
        UserModel user2 = new UserModel("test2@gmail.com", "test2", "test2");
        userService.registerUser(user1);
        userService.registerUser(user2);
        
        // Creating conversation
        ConversationModel conversation = new ConversationModel((long) 1);
        conversationService.createConversation(conversation);
        
        
        // Test : create message
        UserModel user = userService.findUserByUsername("test2");
        MessageModel messageModel = new MessageModel(user, conversation, "msgTest");
        messageService.createMessage(messageModel);
        messageService.getMessagesByConversationId(conversation.getId());
    }
}