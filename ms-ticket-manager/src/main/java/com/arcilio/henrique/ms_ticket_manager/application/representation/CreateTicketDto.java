package com.arcilio.henrique.ms_ticket_manager.application.representation;

import lombok.Data;

@Data
public class CreateTicketDto {
    private String eventId;
    private Double brlTotalAmount;
}
