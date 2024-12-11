package com.javadiscordproject.services;

import com.javadiscordproject.models.Message;
import java.util.Random;
import java.util.logging.Logger;
import java.util.function.Consumer;

public class AutoResponseService {
    private static final Logger LOGGER = Logger.getLogger(AutoResponseService.class.getName());
    private final Random random = new Random();
    private final String[] AUTO_RESPONSES = {
            "Hello!", "How are you?", "That's interesting!",
            "I agree with you", "Could you explain more?",
            "Nice to meet you", "What do you think about that?",
            "Let's discuss this further"
    };

    public void simulateResponse(String currentUser, Consumer<Message> callback) {
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                String response = AUTO_RESPONSES[random.nextInt(AUTO_RESPONSES.length)];
                Message autoMessage = new Message(response, currentUser, false);
                callback.accept(autoMessage);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LOGGER.warning("Response simulation interrupted");
            }
        }).start();
    }
}