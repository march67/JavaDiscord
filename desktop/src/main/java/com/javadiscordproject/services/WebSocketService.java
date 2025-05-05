package com.javadiscordproject.services;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class WebSocketService extends WebSocketClient {

    public WebSocketService(String serverUri) throws Exception {
        super(new URI(serverUri));
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected to server");
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Message received: " + message);
        // Ajouter une méthode pour traiter les messages reçus
        handleIncomingMessage(message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Disconnected: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }

    public void sendMessage(String message) {
        this.send(message);
    }

    private void handleIncomingMessage(String message) {
        // Logique pour afficher ou traiter le message
        System.out.println("Processing message: " + message);
    }

}

