package com.arcilio.henrique.ms_ticket_manager.domain.model;

import com.arcilio.henrique.ms_ticket_manager.application.representation.EventDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("tickets")
@Getter @Setter
public class Ticket {
    @Id
    private String id;
    private EventDto event;
    private Double brlTotalAmount;
    private Double usdTotalAmount;
    private TicketStatus status;
}
