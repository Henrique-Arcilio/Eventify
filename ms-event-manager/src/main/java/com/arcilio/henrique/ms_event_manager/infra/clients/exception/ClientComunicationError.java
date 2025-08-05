package com.arcilio.henrique.ms_event_manager.infra.clients.exception;

public class ClientComunicationError extends RuntimeException {
  public ClientComunicationError(String message) {
    super(message);
  }
}
