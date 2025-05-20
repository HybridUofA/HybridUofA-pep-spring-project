package com.example.exception;

public class UnauthorizedLogin extends RuntimeException {
    public UnauthorizedLogin(String message) {
        super(message);
    }
}
