package com.arcilio.henrique.ms_ticket_manager.application.representation;

import lombok.Data;

@Data
public class CheckPurchasedTicketsDto {
    private String id;
    private String ticketId;
    private String costumerName;
    private String cpf;
    private String costumerMail;
    private Double BRLTotalAmount;
    private Double USDTotalAmount;
}
