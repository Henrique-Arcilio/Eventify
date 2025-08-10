package com.arcilio.henrique.ms_ticket_manager.infra.repository;

import com.arcilio.henrique.ms_ticket_manager.domain.model.Ticket;
import com.arcilio.henrique.ms_ticket_manager.domain.model.TicketStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TicketForSaleRepository extends MongoRepository<Ticket, String> {
    List<Ticket> findByEventIdAndStatus(String eventId, TicketStatus status);

    Page<Ticket> findAllByStatus(TicketStatus ticketStatus, Pageable pageable);
}
