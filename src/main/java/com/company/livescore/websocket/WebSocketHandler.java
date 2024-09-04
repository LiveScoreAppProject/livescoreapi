package com.company.livescore.websocket;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    // Store WebSocket sessions with their associated integer keys
    private final ConcurrentHashMap<WebSocketSession, String> sessionKeys = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Keep the session open without sending a message yet
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Parse the integer from the message
        try {
            String key = message.getPayload();
            // Store the session with the associated key
            sessionKeys.put(session, key);
            // Optionally, you can send an acknowledgment back to the client
            session.sendMessage(new TextMessage("Key received: " + key));
        } catch (NumberFormatException e) {
            // Handle invalid integer input
            session.sendMessage(new TextMessage("Invalid key. Please send an integer."));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessionKeys.remove(session);
    }

    public void broadcastMessage(String key, String message) {
        for (WebSocketSession session : sessionKeys.keySet()) {
            if (sessionKeys.get(session).equals(key)) {
                try {
                    session.sendMessage(new TextMessage(message));
                } catch (Exception e) {
                    sessionKeys.remove(session);
                }
            }
        }
    }
}
