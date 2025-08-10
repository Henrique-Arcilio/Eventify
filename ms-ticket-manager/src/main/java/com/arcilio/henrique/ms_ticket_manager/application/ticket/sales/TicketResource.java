package com.arcilio.henrique.ms_ticket_manager.application.ticket.sales;

import com.arcilio.henrique.ms_ticket_manager.application.docs.TicketResourceDocs;
import com.arcilio.henrique.ms_ticket_manager.application.representation.mapper.PageableMapper;
import com.arcilio.henrique.ms_ticket_manager.application.representation.mapper.TicketMapper;
import com.arcilio.henrique.ms_ticket_manager.application.representation.tickets.*;
import com.arcilio.henrique.ms_ticket_manager.domain.model.Ticket;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
@Tag(name = "Tickets for Sale", description = "Endpoints for managing the tickets available for sale")
public class TicketResource implements TicketResourceDocs {
    public final TicketService ticketService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Override
    public ResponseEntity<Ticket> create(@Valid @RequestBody CreateTicketDto dto){
        Ticket ticket = ticketService.create(dto);
        return ResponseEntity.ok(ticket);
    }

    @GetMapping("/events/{eventId}")
    @Override
    public ResponseEntity<List<GetTicketByEventDto>> getByEventId(@PathVariable String eventId){
        List<Ticket> tickets = ticketService.findByEventId(eventId);
        List<GetTicketByEventDto> ticketDtos = TicketMapper.listOfForSaleDto(tickets);
        return ResponseEntity.ok().body(ticketDtos);
    }


    @GetMapping("/{id}")
    @Override
    public ResponseEntity<GetTicketByIdDto> getById(@PathVariable String id){
        Ticket ticket = ticketService.findById(id);
        GetTicketByIdDto ticketDto = TicketMapper.ticketForSaleDto(ticket);
        return ResponseEntity.ok(ticketDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    @Override
    public ResponseEntity<PageableDto> getAll(Pageable pageable){
        Page<Ticket> tickets = ticketService.findAll(pageable);
        return ResponseEntity.ok(PageableMapper.toDto(tickets));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    @Override
    public ResponseEntity<Void> update(@PathVariable String id, @Valid @RequestBody UpdateTicketDto updateDto){
        ticketService.updatePrice(id, updateDto);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("{id}/cancel")
    @Override
    public ResponseEntity<Void> cancel(@PathVariable String id){
        ticketService.cancel(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("{eventId}/sync")
    @Override
    public ResponseEntity<Void> syncEventUpdates(@PathVariable String eventId){
        ticketService.syncEventUpdates(eventId);
        return ResponseEntity.noContent().build();
    }
}
