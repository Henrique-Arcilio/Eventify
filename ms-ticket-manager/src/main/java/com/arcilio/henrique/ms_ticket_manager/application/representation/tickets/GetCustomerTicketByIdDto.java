package com.arcilio.henrique.ms_ticket_manager.application.representation.tickets;

import lombok.Data;

@Data
public class GetCustomerTicketByIdDto {
    private String eventId;
    private String purchasedTicketId;
    private String costumerName;
    private String cpf;
    private String customerMail;
    private Double BRLTotalAmount;
    private Double USDTotalAmount;
}
