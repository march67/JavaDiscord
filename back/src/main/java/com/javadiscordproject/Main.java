package com.javadiscordproject;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.javadiscordproject.business.service.api.ConversationService;
import com.javadiscordproject.business.service.api.MessageService;
import com.javadiscordproject.business.service.api.UserConversationService;
import com.javadiscordproject.business.service.api.UserService;
import com.javadiscordproject.domain.model.ConversationModel;
import com.javadiscordproject.domain.model.MessageModel;
import com.javadiscordproject.domain.model.UserConversationModel;
import com.javadiscordproject.domain.model.UserModel;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        
        // Test create conversation
    	
//        // Retrieve spring context
//        ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);
//        ConversationService conversationService = context.getBean(ConversationService.class);
//        UserService userService = context.getBean(UserService.class);
//        MessageService messageService = context.getBean(MessageService.class);
//        UserConversationService userConversationService = context.getBean(UserConversationService.class);
//        
//        // Creating users
//        UserModel user1 = new UserModel("test1@gmail.com", "test1", "test1");
//        UserModel user2 = new UserModel("test2@gmail.com", "test2", "test2");
//        userService.saveUser(user1);
//        userService.saveUser(user2);
//        
//        // Creating conversation
//        ConversationModel conversation = new ConversationModel();
//        conversationService.saveConversation(conversation);
//        
//        // Creating user conversation
//        UserConversationModel userConversation1 = new UserConversationModel(user1, conversation);
//        UserConversationModel userConversation2 = new UserConversationModel(user2, conversation);
//        userConversationService.saveUserConversation(userConversation1);
//        userConversationService.saveUserConversation(userConversation2);
//        
//        
//        // Test : create message
//        MessageModel messageModel1 = new MessageModel(user1, conversation, "message 1 de user 1");
//        MessageModel messageModel2 = new MessageModel(user1, conversation, "message 2 de user 1");
//        MessageModel messageModel3 = new MessageModel(user2, conversation, "message 1 de user 2");
//        messageService.saveMessage(messageModel1);
//        messageService.saveMessage(messageModel2);
//        messageService.saveMessage(messageModel3);
//        List<MessageModel> messages = messageService.getMessagesByConversationId(conversation.getId());
//        for (MessageModel message : messages) {
//        	String messageContent = message.getMessageContent();
//        	System.out.println("Contenu du message " + messageContent);
//        }
    }
}