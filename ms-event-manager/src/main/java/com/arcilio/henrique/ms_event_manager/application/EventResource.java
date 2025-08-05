package com.arcilio.henrique.ms_event_manager.application;

import com.arcilio.henrique.ms_event_manager.application.representation.CreateEventDto;
import com.arcilio.henrique.ms_event_manager.domain.model.Event;
import com.arcilio.henrique.ms_event_manager.infra.clients.ViaCepClient;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/v1/events")
@RequiredArgsConstructor
public class EventResource {

    private final EventService eventService;

    @PostMapping("/create-event")
    public ResponseEntity<Event> create(@Valid @RequestBody CreateEventDto dto){
        log.info("EventResource create method accessed");  
        Event event = eventService.createEvent(dto.fromDto());
        return ResponseEntity.ok(event);
    }

}
