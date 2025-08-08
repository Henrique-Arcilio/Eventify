package com.arcilio.henrique.ms_ticket_manager.application.exception.security;

import org.springframework.security.core.AuthenticationException;

public class InvalidJwtAuthenticationException extends AuthenticationException {
    public InvalidJwtAuthenticationException(String message) {
        super(message);
    }
}
