package com.javadiscordproject.main.converters;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.javadiscordproject.models.Message;

/**
 * Custom serializer for outgoing message objects to the server.
 * Maps the client's field names (sender, content) to the server's (from, text).
 */
public class MessageSerializer extends StdSerializer<Message> {

    private static final long serialVersionUID = 1L;

    public MessageSerializer() {
        this(null);
    }

    public MessageSerializer(Class<Message> t) {
        super(t);
    }

    @Override
    public void serialize(Message message, JsonGenerator gen, SerializerProvider provider)
            throws IOException {

        gen.writeStartObject();

        // Write fields with the names the server expects
        gen.writeStringField("from", message.getSender());
        gen.writeStringField("text", message.getContent());
        gen.writeBooleanField("isUser", message.isUser());

        // Add time if needed - system timestamp will be used
        gen.writeStringField("time", String.valueOf(System.currentTimeMillis()));

        gen.writeEndObject();
    }
}