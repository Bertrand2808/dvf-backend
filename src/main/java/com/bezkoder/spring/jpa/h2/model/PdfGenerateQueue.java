package com.bezkoder.spring.jpa.h2.model;

public class PdfGenerateQueue {

    private String message;

    public PdfGenerateQueue(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "PdfGenerateQueue{" +
                "message='" + message + '\'' +
                '}';
    }
}