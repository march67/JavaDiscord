package com.javadiscordproject.main.converters;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.javadiscordproject.models.Message;

/**
 * Custom message converter that handles the field name differences
 * between client and server message classes.
 */
public class ClientMessageConverter extends MappingJackson2MessageConverter {

    public ClientMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();

        // Configure Jackson for field name mapping
        SimpleModule module = new SimpleModule();
        module.addSerializer(Message.class, new MessageSerializer());
        module.addDeserializer(Message.class, new MessageDeserializer());

        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // Register the module
        objectMapper.registerModule(module);

        // Set the custom object mapper
        setObjectMapper(objectMapper);
    }

    /**
     * Creates and returns a properly configured converter
     */
    public static MessageConverter createConverter() {
        return new ClientMessageConverter();
    }
}