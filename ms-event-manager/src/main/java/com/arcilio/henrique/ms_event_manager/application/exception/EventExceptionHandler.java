package com.arcilio.henrique.ms_event_manager.application.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class EventExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> invalidArgument(MethodArgumentNotValidException exception,
                                                        HttpServletRequest request, BindingResult result){
        log.error("Api error: ", exception);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ErrorMessage(request,
                HttpStatus.UNPROCESSABLE_ENTITY, "Invalid fields", result ));
    }
}
