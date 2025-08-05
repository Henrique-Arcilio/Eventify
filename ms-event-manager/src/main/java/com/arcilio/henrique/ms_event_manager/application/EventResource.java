package com.arcilio.henrique.ms_event_manager.application;

import com.arcilio.henrique.ms_event_manager.application.representation.CreateEventDto;
import com.arcilio.henrique.ms_event_manager.domain.model.Event;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/get-event/{id}")
    public ResponseEntity<Event> getById(@PathVariable String id){
        log.info("EventResource getById method accessed");
        Event event = eventService.findById(id);
        return ResponseEntity.ok(event);
    }

}
