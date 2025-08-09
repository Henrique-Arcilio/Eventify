package com.arcilio.henrique.ms_ticket_manager.application.exception.event;

public class CancelledEventException extends RuntimeException {
    public CancelledEventException(String message) {
        super(message);
    }
}