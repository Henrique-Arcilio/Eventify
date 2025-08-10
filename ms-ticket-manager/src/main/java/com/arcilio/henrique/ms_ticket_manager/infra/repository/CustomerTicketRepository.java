package com.arcilio.henrique.ms_ticket_manager.infra.repository;

import com.arcilio.henrique.ms_ticket_manager.domain.model.TicketStatus;
import com.arcilio.henrique.ms_ticket_manager.domain.model.CustomerTicket;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerTicketRepository extends MongoRepository<CustomerTicket, String> {
    List<CustomerTicket> findByEventIdAndStatus(String eventId, TicketStatus ticketStatus);

    Optional<CustomerTicket> findByIdAndUserId(String id, String userId);
}
