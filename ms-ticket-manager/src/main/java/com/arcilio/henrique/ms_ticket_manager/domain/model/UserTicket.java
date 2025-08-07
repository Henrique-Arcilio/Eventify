package com.arcilio.henrique.ms_ticket_manager.domain.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("usersTickets")
@Getter @Setter
public class UserTicket {
    @Id
    private String id;
    private String eventId;
    private String ticketId;
    private String costumerName;
    private String cpf;
    private String costumerMail;
    private Double BRLTotalAmount;
    private Double USDTotalAmount;
}
