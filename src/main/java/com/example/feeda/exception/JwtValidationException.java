package com.example.feeda.exception;

import lombok.Getter;

@Getter
public class JwtValidationException extends RuntimeException {
    private final int statusCode;

    public JwtValidationException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
