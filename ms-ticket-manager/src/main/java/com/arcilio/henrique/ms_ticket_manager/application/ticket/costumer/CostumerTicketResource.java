package com.arcilio.henrique.ms_ticket_manager.application.ticket.costumer;

import com.arcilio.henrique.ms_ticket_manager.application.representation.mapper.TicketMapper;
import com.arcilio.henrique.ms_ticket_manager.application.representation.tickets.GetUserTicketByIdDto;
import com.arcilio.henrique.ms_ticket_manager.application.representation.tickets.PurchasedTicketsByEventDto;
import com.arcilio.henrique.ms_ticket_manager.application.ticket.TicketService;
import com.arcilio.henrique.ms_ticket_manager.domain.model.UserTicket;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customer-tickets/")
@RequiredArgsConstructor
public class CostumerTicketResource {

    public final TicketService ticketService;

    @GetMapping("/events/{eventId}")
    public ResponseEntity<List<PurchasedTicketsByEventDto>> getByEventId(@PathVariable String eventId){
        List<UserTicket> tickets = ticketService.findPurchasedByEvent(eventId);
        List<PurchasedTicketsByEventDto> ticketDtos = TicketMapper.listOfPurchasedDto(tickets);
        return ResponseEntity.ok().body(ticketDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetUserTicketByIdDto> getById(@PathVariable String id){
        UserTicket ticket = ticketService.findUserTicketById(id);
        GetUserTicketByIdDto ticketDto = TicketMapper.ticketForSaleDto(ticket);
        return ResponseEntity.ok(ticketDto);
    }

    @PostMapping("/{id}")
    public ResponseEntity<UserTicket> buy(@PathVariable String id,  @AuthenticationPrincipal UserDetails userDetails){
        UserTicket newTicket =  ticketService.createUserTicket(id, userDetails);
        return ResponseEntity.ok(newTicket);
    }

    @PatchMapping("{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable String id){
        ticketService.cancelUserTicket(id);
        return ResponseEntity.noContent().build();
    }
}
