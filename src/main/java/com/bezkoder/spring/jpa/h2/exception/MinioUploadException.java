package com.bezkoder.spring.jpa.h2.exception;

public class MinioUploadException extends Exception {
    public MinioUploadException(String message) {
        super(message);
    }

    public MinioUploadException(String message, Throwable cause) {
        super(message, cause);
    }
}
