package com.bezkoder.spring.jpa.h2.config;

import com.bezkoder.spring.jpa.h2.exception.WebSocketNotificationException;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
@Component
public class MyWebSocketHandler extends TextWebSocketHandler {

    @Getter
    private static final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        // This method is intentionally left empty.
        // In this application, we do not need to handle incoming WebSocket messages.
    }

    public void sendPdfGeneratedNotification(String base64Pdf) {
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(base64Pdf));
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new WebSocketNotificationException("Erreur lors de l'envoi de la notification WebSocket.", e);
                }
            }
        }
    }
}