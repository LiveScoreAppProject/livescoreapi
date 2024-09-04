package com.company.livescore.services;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.company.livescore.websocket.WebSocketHandler;

@Service
public class KafkaConsumerService {

    private final WebSocketHandler webSocketHandler;

    public KafkaConsumerService(WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @KafkaListener(topics = "live_events", groupId = "event-consumer-group")
    public void listen(String message) {

        String[] parts = message.split(":", 2);
        if (parts.length == 2) {
            try {
                int key = Integer.parseInt(parts[0]);
                String event = parts[1];
                webSocketHandler.broadcastMessage(key, event);
            } catch (NumberFormatException e) {
                System.out.println("Invalid key in Kafka message: " + parts[0]);
            }
        } else {

            System.out.println("Invalid message format: " + message);
        }
    }
}