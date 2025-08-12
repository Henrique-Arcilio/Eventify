package com.arcilio.henrique.ms_ticket_manager.domain.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("customer-tickets")
@Getter @Setter
public class CustomerTicket {
    @Id
    private String id;
    private String userId;
    private String eventId;
    private String purchasedTicketId;
    private String customerName;
    private String cpf;
    private String customerMail;
    private Double brlTotalAmount;
    private Double usdTotalAmount;
    private TicketStatus status;
}
