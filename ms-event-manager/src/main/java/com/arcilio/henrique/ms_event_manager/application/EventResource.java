package com.arcilio.henrique.ms_event_manager.application;

import com.arcilio.henrique.ms_event_manager.application.representation.CreateEventDto;
import com.arcilio.henrique.ms_event_manager.application.representation.PagableDto;
import com.arcilio.henrique.ms_event_manager.application.representation.mapper.PageableMapper;
import com.arcilio.henrique.ms_event_manager.domain.model.Event;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @GetMapping("/get-all-events")
    public ResponseEntity<PagableDto> getAll(Pageable pageable){
        Page<Event> events = eventService.findAll(pageable);
        return ResponseEntity.ok().body(PageableMapper.toDto(events));
    }

}
