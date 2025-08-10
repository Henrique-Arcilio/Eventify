package com.arcilio.henrique.ms_event_manager.application.docs;

import com.arcilio.henrique.ms_event_manager.application.representation.CreateEventDto;
import com.arcilio.henrique.ms_event_manager.application.representation.PageableDto;
import com.arcilio.henrique.ms_event_manager.application.representation.UpdateEventDto;
import com.arcilio.henrique.ms_event_manager.domain.model.Event;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface EventResourceDocs {

    ResponseEntity<Event> create(@Valid @RequestBody CreateEventDto dto);

    ResponseEntity<Event> getById(@PathVariable String id);

    ResponseEntity<PageableDto> getAll(Pageable pageable);

    ResponseEntity<PageableDto> getAllSorted(Pageable pageable);

    ResponseEntity<Void> update(@PathVariable String id, @Valid @RequestBody UpdateEventDto updateDto);

    ResponseEntity<Void> cancel(@PathVariable String id);
}
