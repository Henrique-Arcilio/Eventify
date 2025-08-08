package com.arcilio.henrique.ms_ticket_manager.application.ticket;

import com.arcilio.henrique.ms_ticket_manager.application.representation.tickets.*;
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

    @GetMapping("tickets-for-sale/event/{eventId}")
    public ResponseEntity<List<TicketForSaleByEventDto>> checkForSaleByEventId(@PathVariable String eventId){
        List<TicketForSale> tickets = ticketService.findForSaleByEvent(eventId);
        List<TicketForSaleByEventDto> ticketDtos = TicketMapper.listOfForSaleDto(tickets);
        return ResponseEntity.ok().body(ticketDtos);
    }
    @GetMapping("purchased-tickets/event/{eventId}")
    public ResponseEntity<List<PurchasedTicketsByEventDto>> checkPurchasedByEventId(@PathVariable String eventId){
        List<UserTicket> tickets = ticketService.findPurchasedByEvent(eventId);
        List<PurchasedTicketsByEventDto> ticketDtos = TicketMapper.listOfPurchasedDto(tickets);
        return ResponseEntity.ok().body(ticketDtos);
    }

    @GetMapping("tickets-for-sale/{id}")
    public ResponseEntity<GetTicketForSaleByIdDto> getForSaleTicketById(@PathVariable String id){
        TicketForSale ticket = ticketService.findForSaleById(id);
        GetTicketForSaleByIdDto ticketDto = TicketMapper.ticketForSaleDto(ticket);
        return ResponseEntity.ok(ticketDto);
    }
    @GetMapping("user-ticket/{id}")
    public ResponseEntity<GetUserTicketByIdDto> getUserTicket(@PathVariable String id){
        UserTicket ticket = ticketService.findUserTicketById(id);
        GetUserTicketByIdDto ticketDto = TicketMapper.ticketForSaleDto(ticket);
        return ResponseEntity.ok(ticketDto);
    }
}
