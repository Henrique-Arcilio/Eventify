package com.arcilio.henrique.ms_ticket_manager.application.ticket;

import com.arcilio.henrique.ms_ticket_manager.application.representation.CreateTicketDto;
import com.arcilio.henrique.ms_ticket_manager.domain.model.TicketForSale;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketResource {
    public final TicketService ticketService;

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("create-ticket")
    public ResponseEntity<TicketForSale> create(@RequestBody CreateTicketDto dto){
        TicketForSale ticket = ticketService.createTicketForSale(dto);
        return ResponseEntity.ok(ticket);
    }

}
