package com.arcilio.henrique.ms_ticket_manager.application.docs;


import com.arcilio.henrique.ms_ticket_manager.application.representation.tickets.*;
import com.arcilio.henrique.ms_ticket_manager.domain.model.Ticket;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface TicketResourceDocs {
    ResponseEntity<Ticket> create(@Valid @RequestBody CreateTicketDto dto);

    ResponseEntity<List<GetTicketByEventDto>> getByEventId(@PathVariable String eventId);

    ResponseEntity<GetTicketByIdDto> getById(@PathVariable String id);

    public ResponseEntity<PageableDto> getAll(Pageable pageable);

    ResponseEntity<Void> update(@PathVariable String id, @Valid @RequestBody UpdateTicketDto updateDto);

    ResponseEntity<Void> cancel(@PathVariable String id);

    ResponseEntity<Void> syncEventUpdates(@PathVariable String eventId);
}
