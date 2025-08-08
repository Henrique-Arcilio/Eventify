package com.arcilio.henrique.ms_ticket_manager.infra.repository;

import com.arcilio.henrique.ms_ticket_manager.domain.model.TicketForSale;
import com.arcilio.henrique.ms_ticket_manager.domain.model.TicketStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TicketForSaleRepository extends MongoRepository<TicketForSale, String> {
    List<TicketForSale> findByEventIdAndStatus(String eventId, TicketStatus status);
}
