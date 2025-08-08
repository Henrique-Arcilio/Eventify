package com.arcilio.henrique.ms_event_manager.infra.repository;

import com.arcilio.henrique.ms_event_manager.domain.model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventRepository extends MongoRepository<Event, String> {
}
