package com.arcilio.henrique.ms_ticket_manager.application.exception.ticket;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
