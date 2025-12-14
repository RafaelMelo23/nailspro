package com.rafael.nailspro.webapp.service.infra.exception;

public class LoginException extends RuntimeException {
    public LoginException(String message) {
        super(message);
    }
}
