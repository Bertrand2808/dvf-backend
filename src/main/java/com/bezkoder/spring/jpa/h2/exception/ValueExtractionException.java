package com.bezkoder.spring.jpa.h2.exception;

public class ValueExtractionException extends RuntimeException {
    public ValueExtractionException(String message, Throwable cause) {
        super(message, cause);
    }
}
