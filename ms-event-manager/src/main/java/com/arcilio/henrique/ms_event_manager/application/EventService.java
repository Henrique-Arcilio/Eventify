package com.arcilio.henrique.ms_event_manager.application;

import com.arcilio.henrique.ms_event_manager.domain.model.Event;
import com.arcilio.henrique.ms_event_manager.infra.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public Event createEvent(Event event){
        return eventRepository.save(event);
    }
}
