package com.arcilio.henrique.ms_ticket_manager.infra.client;

public class ClientComunicationError extends RuntimeException {
    public ClientComunicationError(String message) {
        super(message);
    }
}
