package com.javadiscordproject.services.api.message;

import com.javadiscordproject.managers.SessionManager;
import com.javadiscordproject.models.MessageDTO;
import com.javadiscordproject.utils.ApiConstants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class MessageService implements IMessageService {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public MessageService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public List<MessageDTO> getConversationMessages(Long conversationId) {
        try {
            String token = SessionManager.getInstance().getToken();
            String url = ApiConstants.MESSAGE_ENDPOINT + "/" + conversationId;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + token)
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response: " + response.body());

            if (response.statusCode() == 200) {
                List<MessageDTO> messages = objectMapper.readValue(
                        response.body(),
                        new TypeReference<List<MessageDTO>>() {}
                );
                return messages;
            } else {
                System.err.println("Failed to fetch messages. Status code: " + response.statusCode());
                return new ArrayList<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public boolean sendMessage(Long conversationId, String messageContent) {
        return false;
    }
}