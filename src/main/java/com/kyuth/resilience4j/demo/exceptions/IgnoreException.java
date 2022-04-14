package com.kyuth.resilience4j.demo.exceptions;

public class IgnoreException extends RuntimeException {

    public IgnoreException(String message) {
        super(message);
    }
}
