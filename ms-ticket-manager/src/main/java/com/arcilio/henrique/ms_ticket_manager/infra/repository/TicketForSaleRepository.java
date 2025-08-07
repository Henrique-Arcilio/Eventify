package com.arcilio.henrique.ms_ticket_manager.infra.repository;

import com.arcilio.henrique.ms_ticket_manager.domain.model.TicketForSale;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TicketForSaleRepository extends MongoRepository<TicketForSale, String> {

}
