package com.arcilio.henrique.ms_ticket_manager.application.ticket.customer;

import com.arcilio.henrique.ms_ticket_manager.application.representation.mapper.TicketMapper;
import com.arcilio.henrique.ms_ticket_manager.application.representation.tickets.GetCustomerTicketByIdDto;
import com.arcilio.henrique.ms_ticket_manager.application.representation.tickets.GetCustomerTicketsByEventDto;
import com.arcilio.henrique.ms_ticket_manager.domain.model.CustomerTicket;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customer-tickets/")
@RequiredArgsConstructor
public class CustomerTicketResource {

    public final CustomerTicketSerivce ticketService;

    @GetMapping("/events/{eventId}")
    public ResponseEntity<List<GetCustomerTicketsByEventDto>> getByEventId(@PathVariable String eventId){
        List<CustomerTicket> tickets = ticketService.findByEventId(eventId);
        List<GetCustomerTicketsByEventDto> ticketDtos = TicketMapper.listOfPurchasedDto(tickets);
        return ResponseEntity.ok().body(ticketDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetCustomerTicketByIdDto> getById(@PathVariable String id){
        CustomerTicket ticket = ticketService.findById(id);
        GetCustomerTicketByIdDto ticketDto = TicketMapper.ticketForSaleDto(ticket);
        return ResponseEntity.ok(ticketDto);
    }

    @PostMapping("/{id}")
    public ResponseEntity<CustomerTicket> buy(@PathVariable String id, @AuthenticationPrincipal UserDetails userDetails){
        CustomerTicket newTicket =  ticketService.create(id, userDetails);
        return ResponseEntity.ok(newTicket);
    }

    @PatchMapping("{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable String id){
        ticketService.cancelUserTicket(id);
        return ResponseEntity.noContent().build();
    }
}
