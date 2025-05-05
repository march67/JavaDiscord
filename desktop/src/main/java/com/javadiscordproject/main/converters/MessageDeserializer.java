package com.javadiscordproject.main.converters;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.javadiscordproject.models.Message;

/**
 * Custom deserializer for incoming message objects from the server.
 * Maps the server's field names (from, text) to the client's (sender, content).
 */
public class MessageDeserializer extends StdDeserializer<Message> {

    private static final long serialVersionUID = 1L;

    public MessageDeserializer() {
        this(null);
    }

    public MessageDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Message deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {

        JsonNode node = jp.getCodec().readTree(jp);

        // Extract fields from JSON, with fallbacks for different field names
        String content = node.has("text") ? node.get("text").asText() :
                (node.has("content") ? node.get("content").asText() : "");

        String sender = node.has("from") ? node.get("from").asText() :
                (node.has("sender") ? node.get("sender").asText() : "");

        boolean isUser = node.has("isUser") ? node.get("isUser").asBoolean() : false;

        // Create a new Message instance with the mapped fields
        return new Message(content, sender, isUser);
    }
}