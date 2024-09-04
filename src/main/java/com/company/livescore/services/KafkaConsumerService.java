package com.company.livescore.services;

import org.apache.kafka.clients.consumer.ConsumerRecord;
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
    public void listen(ConsumerRecord<String, String> record) {
        String key = record.key();
        String value = record.value();

        if (key != null && value != null) {
            webSocketHandler.broadcastMessage(key, value);
        } else {
            System.out.println("Received record with null key or value");
        }
    }
}