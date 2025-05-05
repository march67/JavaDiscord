package com.javadiscordproject.main.handlers;

import java.lang.reflect.Type;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

import com.javadiscordproject.models.ChatMessage;

public class MyStompSessionHandler implements StompSessionHandler {

    private Logger logger = LogManager.getLogger(MyStompSessionHandler.class);

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return ChatMessage.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        ChatMessage msg = (ChatMessage) payload;
        logger.info("Received : " + msg.getContent() + " from : " + msg.getSender());
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        logger.info("New session established : " + session.getSessionId());
        session.subscribe("/topic/messages", this);
        logger.info("Subscribed to /topic/messages");
        session.send("/app/chat", getSampleMessage());
        logger.info("Message sent to websocket server");
    }

    private ChatMessage getSampleMessage() {
        return new ChatMessage("client", "hello");
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload,
                                Throwable exception) {
        logger.error("Got an exception", exception);
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        logger.error("Got an exception", exception);
    }

}
