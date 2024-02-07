package com.bezkoder.spring.jpa.h2.model;

import io.swagger.v3.oas.annotations.media.Schema;

public class PdfGenerateQueue {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
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