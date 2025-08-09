package com.arcilio.henrique.ms_event_manager.application.exception;

import com.arcilio.henrique.ms_event_manager.infra.clients.exception.ActiveTicketException;
import com.arcilio.henrique.ms_event_manager.infra.clients.exception.CepNotFoundException;
import com.arcilio.henrique.ms_event_manager.infra.clients.exception.ClientComunicationError;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorMessage> invalidDate(HttpServletRequest request){

        log.error("Api error: ");

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ErrorMessage(request,
                HttpStatus.UNPROCESSABLE_ENTITY, "Invalid date format. Please use: yyyy-MM-dd'T'HH:mm:ss"));
    }

    @ExceptionHandler(CepNotFoundException.class)
    public ResponseEntity<ErrorMessage> cepNotFound

            (CepNotFoundException exception, HttpServletRequest request){

        log.error("Api error: ", exception);

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErrorMessage(request,HttpStatus.UNPROCESSABLE_ENTITY, exception.getMessage()));
    }

    @ExceptionHandler(ClientComunicationError.class)
    public ResponseEntity<ErrorMessage> clientComunicationError
            (ClientComunicationError exception, HttpServletRequest request){

        log.error("Api error: ", exception);

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new ErrorMessage(request,HttpStatus.SERVICE_UNAVAILABLE, exception.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessage> resourceNotFound
            (ResourceNotFoundException exception, HttpServletRequest request){

        log.error("Api error: ", exception);

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(request,HttpStatus.NOT_FOUND, exception.getMessage()));
    }

    @ExceptionHandler(ActiveTicketException.class)
    public ResponseEntity<ErrorMessage> activeTicket(ActiveTicketException exception,
                                                        HttpServletRequest request){
        log.error("Api error: ", exception);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorMessage(request,
                HttpStatus.CONFLICT, exception.getMessage(), exception.getActiveTickets()));
    }

    @ExceptionHandler(CancelledEventException.class)
    public ResponseEntity<ErrorMessage> cancelledEventException(CancelledEventException exception,
                                                     HttpServletRequest request){
        log.error("Api error: ", exception);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorMessage(request,
                HttpStatus.CONFLICT, exception.getMessage()));
    }

}
