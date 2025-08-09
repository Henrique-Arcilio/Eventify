package com.arcilio.henrique.ms_event_manager.application.exception;

public class CancelledEventException extends RuntimeException {
    public CancelledEventException(String message) {
        super(message);
    }
}
