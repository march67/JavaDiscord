package com.javadiscordproject.main.runners;

import org.springframework.boot.CommandLineRunner;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import com.javadiscordproject.main.handlers.MyStompSessionHandler;

@Component
public class Runner implements CommandLineRunner {

    private static String URL = "ws://localhost:8080/chat";

    @Override
    public void run(String... args) throws Exception {

        System.out.println("Run...");

        WebSocketClient webSocketClient = new StandardWebSocketClient();

        WebSocketStompClient webSocketStompClient = new WebSocketStompClient(webSocketClient);
        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSessionHandler stompSessionHandler = new MyStompSessionHandler();
        webSocketStompClient.connectAsync(URL, stompSessionHandler);

    }

}
