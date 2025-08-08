package com.arcilio.henrique.ms_ticket_manager.application.representation.tickets;

import lombok.Data;

@Data
public class TicketForSaleByEventDto {
    private String id;
    private String eventName;
    private Double brltotalAmount;
    private Double usdtotalAmount;
}
