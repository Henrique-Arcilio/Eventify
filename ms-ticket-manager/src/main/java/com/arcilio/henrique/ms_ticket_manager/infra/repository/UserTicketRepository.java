package com.arcilio.henrique.ms_ticket_manager.infra.repository;

import com.arcilio.henrique.ms_ticket_manager.domain.model.UserTicket;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserTicketRepository extends MongoRepository<UserTicket, String> {
}
