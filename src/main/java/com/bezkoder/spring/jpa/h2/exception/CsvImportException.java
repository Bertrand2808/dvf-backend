package com.bezkoder.spring.jpa.h2.exception;

public class CsvImportException extends RuntimeException {
    public CsvImportException(String message, Throwable cause) {
        super(message, cause);
    }
}
