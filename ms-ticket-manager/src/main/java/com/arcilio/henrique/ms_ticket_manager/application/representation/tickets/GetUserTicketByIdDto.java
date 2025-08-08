package com.arcilio.henrique.ms_ticket_manager.application.representation.tickets;

import lombok.Data;

@Data
public class GetUserTicketByIdDto {
    private String eventId;
    private String ticketId;
    private String costumerName;
    private String cpf;
    private String costumerMail;
    private Double BRLTotalAmount;
    private Double USDTotalAmount;
}
