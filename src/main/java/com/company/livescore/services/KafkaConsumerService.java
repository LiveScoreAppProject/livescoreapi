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

    @KafkaListener(topics = "live-events", groupId = "event-consumer-group")
    public void listen(String message) {
        System.out.println(message);
        webSocketHandler.broadcastMessage(message);
    }

}