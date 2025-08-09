package com.arcilio.henrique.ms_ticket_manager.infra.repository;

import com.arcilio.henrique.ms_ticket_manager.domain.model.TicketStatus;
import com.arcilio.henrique.ms_ticket_manager.domain.model.CustomerTicket;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserTicketRepository extends MongoRepository<CustomerTicket, String> {
    List<CustomerTicket> findByEventIdAndStatus(String eventId, TicketStatus ticketStatus);
}
