package com.arcilio.henrique.ms_ticket_manager.application.exception.security;

import com.arcilio.henrique.ms_ticket_manager.application.exception.ticket.ResourceNotFoundException;
import com.arcilio.henrique.ms_ticket_manager.infra.client.ClientComunicationError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TicketExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> invalidArgument(MethodArgumentNotValidException exception,
                                                        HttpServletRequest request, BindingResult result){
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ErrorMessage(request,
                HttpStatus.UNPROCESSABLE_ENTITY, "Invalid fields", result ));
    }


    @ExceptionHandler(InvalidJwtAuthenticationException.class)
    public ResponseEntity<ErrorMessage> invalidJwtAuthentication
            (InvalidJwtAuthenticationException exception, HttpServletRequest request){
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErrorMessage(request, HttpStatus.FORBIDDEN, exception.getMessage() ));
    }
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ErrorMessage> usernameAlreadyExists
            (UsernameAlreadyExistsException exception, HttpServletRequest request){
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErrorMessage(request, HttpStatus.FORBIDDEN, exception.getMessage() ));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessage> resourceNotFound
            (ResourceNotFoundException exception, HttpServletRequest request){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(request, HttpStatus.NOT_FOUND, exception.getMessage() ));
    }


    @ExceptionHandler(ClientComunicationError.class)
    public ResponseEntity<ErrorMessage> clientComunicationError
            (ClientComunicationError exception, HttpServletRequest request){

        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body(new ErrorMessage(request,HttpStatus.BAD_GATEWAY, exception.getMessage()));
    }
}
