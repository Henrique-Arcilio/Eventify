package com.arcilio.henrique.ms_ticket_manager.application.exception.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TicketExceptionHandler {
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
}
