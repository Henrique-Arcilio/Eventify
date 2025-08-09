package com.arcilio.henrique.ms_ticket_manager.application.ticket.sales;

import com.arcilio.henrique.ms_ticket_manager.application.representation.mapper.TicketMapper;
import com.arcilio.henrique.ms_ticket_manager.application.representation.tickets.CreateTicketDto;
import com.arcilio.henrique.ms_ticket_manager.application.representation.tickets.GetTicketByIdDto;
import com.arcilio.henrique.ms_ticket_manager.application.representation.tickets.GetTicketByEventDto;
import com.arcilio.henrique.ms_ticket_manager.application.representation.tickets.UpdateTicketDto;
import com.arcilio.henrique.ms_ticket_manager.domain.model.Ticket;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketResource {
    public final TicketService ticketService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Ticket> create(@Valid @RequestBody CreateTicketDto dto){
        Ticket ticket = ticketService.create(dto);
        return ResponseEntity.ok(ticket);
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<GetTicketByEventDto>> getByEventId(@PathVariable String eventId){
        List<Ticket> tickets = ticketService.findByEventId(eventId);
        List<GetTicketByEventDto> ticketDtos = TicketMapper.listOfForSaleDto(tickets);
        return ResponseEntity.ok().body(ticketDtos);
    }


    @GetMapping("/{id}")
    public ResponseEntity<GetTicketByIdDto> getById(@PathVariable String id){
        Ticket ticket = ticketService.findById(id);
        GetTicketByIdDto ticketDto = TicketMapper.ticketForSaleDto(ticket);
        return ResponseEntity.ok(ticketDto);
    }

    @GetMapping
    public ResponseEntity<List<Ticket>> getAll(){
        List<Ticket> tickets = ticketService.findAll();
        return ResponseEntity.ok(tickets);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable String id, @Valid @RequestBody UpdateTicketDto updateDto){
        ticketService.updatePrice(id, updateDto);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable String id){
        ticketService.cancel(id);
        return ResponseEntity.noContent().build();
    }

}
