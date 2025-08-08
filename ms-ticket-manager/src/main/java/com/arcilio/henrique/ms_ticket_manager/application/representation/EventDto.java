package com.arcilio.henrique.ms_ticket_manager.application.representation;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventDto {
    private String id;
    private String eventName;
    private LocalDateTime dateTime;
    private String cep;
    private String logradouro;
    private String bairro;
    private String cidade;
    private String uf;
}
