package com.arcilio.henrique.ms_ticket_manager.application.ticket;

import com.arcilio.henrique.ms_ticket_manager.application.representation.tickets.TicketForSaleByEventDto;
import com.arcilio.henrique.ms_ticket_manager.application.representation.tickets.GetForSaleTicketByIdDto;
import com.arcilio.henrique.ms_ticket_manager.application.representation.tickets.PurchasedTicketsByEventDto;
import com.arcilio.henrique.ms_ticket_manager.application.representation.tickets.CreateTicketDto;
import com.arcilio.henrique.ms_ticket_manager.application.representation.mapper.TicketMapper;
import com.arcilio.henrique.ms_ticket_manager.domain.model.TicketForSale;
import com.arcilio.henrique.ms_ticket_manager.domain.model.UserTicket;
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
    public ResponseEntity<List<TicketForSaleByEventDto>> checkForSaleByEventId(@PathVariable String eventId){
        List<TicketForSale> tickets = ticketService.findForSaleByEvent(eventId);
        List<TicketForSaleByEventDto> ticketDtos = TicketMapper.listOfForSaleDto(tickets);
        return ResponseEntity.ok().body(ticketDtos);
    }
    @GetMapping("purchased-tickets/{eventId}")
    public ResponseEntity<List<PurchasedTicketsByEventDto>> checkPurchasedByEventId(@PathVariable String eventId){
        List<UserTicket> tickets = ticketService.findPurchasedByEvent(eventId);
        List<PurchasedTicketsByEventDto> ticketDtos = TicketMapper.listOfPurchasedDto(tickets);
        return ResponseEntity.ok().body(ticketDtos);
    }
}
