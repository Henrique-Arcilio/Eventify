package com.arcilio.henrique.ms_ticket_manager.application.ticket;

import com.arcilio.henrique.ms_ticket_manager.application.representation.CreateTicketDto;
import com.arcilio.henrique.ms_ticket_manager.domain.model.TicketForSale;
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
    @PostMapping("create-ticket")
    public ResponseEntity<TicketForSale> create(@Valid @RequestBody CreateTicketDto dto){
        TicketForSale ticket = ticketService.createTicketForSale(dto);
        return ResponseEntity.ok(ticket);
    }

    @GetMapping("tickets-for-sale/{eventId}")
    public ResponseEntity<List<TicketForSale>> checkForSaleByEventId(@PathVariable String eventId){
        List<TicketForSale> tickets = ticketService.findAByEventId(eventId);
        return ResponseEntity.ok().body(tickets);
    }

}
