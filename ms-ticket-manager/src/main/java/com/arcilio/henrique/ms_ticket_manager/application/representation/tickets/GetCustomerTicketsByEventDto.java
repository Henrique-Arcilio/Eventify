package com.arcilio.henrique.ms_ticket_manager.application.representation.tickets;

import lombok.Data;

@Data
public class GetCustomerTicketsByEventDto {
    private String id;
    private String purchasedTicketId;
    private String customerName;
    private String cpf;
    private String customerMail;
    private Double brlTotalAmount;
    private Double usdTotalAmount;
}
