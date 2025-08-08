package com.arcilio.henrique.ms_ticket_manager.application.exception.security;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(String message) {
        super(message);
    }
}
