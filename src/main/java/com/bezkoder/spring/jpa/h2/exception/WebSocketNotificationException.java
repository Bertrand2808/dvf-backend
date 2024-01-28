package com.bezkoder.spring.jpa.h2.exception;

public class WebSocketNotificationException extends RuntimeException {
    public WebSocketNotificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
