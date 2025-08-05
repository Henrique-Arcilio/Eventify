package com.arcilio.henrique.ms_event_manager.infra.clients.exception;

public class CepNotFoundException extends RuntimeException {
    public CepNotFoundException(String message) {
        super(message);
    }
}
