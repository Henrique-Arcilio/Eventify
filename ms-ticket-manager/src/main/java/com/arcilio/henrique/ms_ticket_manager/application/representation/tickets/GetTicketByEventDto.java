package com.arcilio.henrique.ms_ticket_manager.application.representation.tickets;

import lombok.Data;

@Data
public class GetTicketByEventDto {
    private String id;
    private String eventName;
    private Double brlTotalAmount;
    private Double usdTotalAmount;
}
