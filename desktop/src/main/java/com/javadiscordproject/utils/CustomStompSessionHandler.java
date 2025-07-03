package com.javadiscordproject.utils;

import com.javadiscordproject.models.ChatMessageDTO;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

import java.lang.reflect.Type;

import static java.rmi.server.LogStream.log;

public class CustomStompSessionHandler implements StompSessionHandler {
    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        log("Connection established");
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        log("Exception: " + exception.getMessage());
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        log("Transport error: " + exception.getMessage());
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return ChatMessageDTO.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        // Not used as we subscribe manually
    }
}