package com.arcilio.henrique.ms_ticket_manager.infra.repository;

import com.arcilio.henrique.ms_ticket_manager.domain.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);
}

