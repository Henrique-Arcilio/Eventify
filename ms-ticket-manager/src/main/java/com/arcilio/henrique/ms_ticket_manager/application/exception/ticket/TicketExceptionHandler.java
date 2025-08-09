package com.arcilio.henrique.ms_ticket_manager.application.exception.ticket;

import com.arcilio.henrique.ms_ticket_manager.application.exception.ErrorMessage;
import com.arcilio.henrique.ms_ticket_manager.application.exception.event.CancelledEventException;
import com.arcilio.henrique.ms_ticket_manager.infra.client.ClientComunicationError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TicketExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessage> resourceNotFound
            (ResourceNotFoundException exception, HttpServletRequest request){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(request, HttpStatus.NOT_FOUND, exception.getMessage() ));
    }

    @ExceptionHandler(CancelledEventException.class)
    public ResponseEntity<ErrorMessage> cancelledEvent
            (CancelledEventException exception, HttpServletRequest request){

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorMessage(request,HttpStatus.CONFLICT, exception.getMessage()));
    }

    @ExceptionHandler(ClientComunicationError.class)
    public ResponseEntity<ErrorMessage> clientComunicationError
            (ClientComunicationError exception, HttpServletRequest request){

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new ErrorMessage(request,HttpStatus.SERVICE_UNAVAILABLE, exception.getMessage()));
    }
}
