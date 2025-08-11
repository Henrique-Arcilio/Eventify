package com.arcilio.henrique.ms_ticket_manager.application.ticket.customer;

import com.arcilio.henrique.ms_ticket_manager.application.docs.CustomerTicketResourceDocs;
import com.arcilio.henrique.ms_ticket_manager.application.representation.mapper.PageableMapper;
import com.arcilio.henrique.ms_ticket_manager.application.representation.mapper.TicketMapper;
import com.arcilio.henrique.ms_ticket_manager.application.representation.tickets.GetCustomerTicketByIdDto;
import com.arcilio.henrique.ms_ticket_manager.application.representation.tickets.GetCustomerTicketsByEventDto;
import com.arcilio.henrique.ms_ticket_manager.application.representation.tickets.PageableDto;
import com.arcilio.henrique.ms_ticket_manager.domain.model.CustomerTicket;
import com.arcilio.henrique.ms_ticket_manager.domain.model.Ticket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/customer-tickets")
@RequiredArgsConstructor
public class CustomerTicketResource implements CustomerTicketResourceDocs {

    public final CustomerTicketSerivce ticketService;

    @GetMapping("/events/{eventId}")
    public ResponseEntity<List<GetCustomerTicketsByEventDto>> getByEventId(@PathVariable String eventId){
        log.info("CustomerTicketResource getByEventId method accessed");
        List<CustomerTicket> tickets = ticketService.findByEventId(eventId);
        List<GetCustomerTicketsByEventDto> ticketDtos = TicketMapper.listOfPurchasedDto(tickets);
        return ResponseEntity.ok().body(ticketDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetCustomerTicketByIdDto> getById(@PathVariable String id){
        log.info("CustomerTicketResource getById method accessed");
        CustomerTicket ticket = ticketService.findById(id);
        GetCustomerTicketByIdDto ticketDto = TicketMapper.ticketForSaleDto(ticket);
        return ResponseEntity.ok(ticketDto);
    }

    @GetMapping
    public ResponseEntity<PageableDto> findAll(Pageable pageable, @AuthenticationPrincipal UserDetails userDetails){
        log.info("CustomerTicketResource findAll method accessed");
        Page<CustomerTicket> tickets = ticketService.findAll(pageable, userDetails);
        return ResponseEntity.ok(PageableMapper.toDto(tickets));
    }

    @PostMapping("/{id}")
    public ResponseEntity<CustomerTicket> buy(@PathVariable String id, @AuthenticationPrincipal UserDetails userDetails){
        log.info("CustomerTicketResource buy method accessed");
        CustomerTicket newTicket =  ticketService.create(id, userDetails);
        return ResponseEntity.ok(newTicket);
    }

    @PatchMapping("{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable String id, @AuthenticationPrincipal UserDetails userDetails){
        log.info("CustomerTicketResource cancel method accessed");
        ticketService.cancelUserTicket(id, userDetails);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/sales")
    public ResponseEntity<PageableDto> getAllForSale(Pageable pageable){
        log.info("CustomerTicketResource getAllForSale method accessed");
        Page<Ticket> tickets = ticketService.findAllForSale(pageable);
        return ResponseEntity.ok(PageableMapper.toDto(tickets));
    }
}
