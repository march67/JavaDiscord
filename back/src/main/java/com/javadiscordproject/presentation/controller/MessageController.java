package com.javadiscordproject.presentation.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.javadiscordproject.domain.model.InputMessage;
import com.javadiscordproject.domain.model.OutputMessage;


@Controller
@CrossOrigin(origins = "*")
public class MessageController {

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public OutputMessage send(InputMessage message) {
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        System.out.println("Message : " + message.getFrom() + " " + message.getText() + " " + time);
        return new OutputMessage(message.getFrom(), message.getText(), time);
    }

}

