package com.arcilio.henrique.ms_ticket_manager.application.ticket.sales;

import com.arcilio.henrique.ms_ticket_manager.application.representation.mapper.TicketMapper;
import com.arcilio.henrique.ms_ticket_manager.application.representation.tickets.CreateTicketDto;
import com.arcilio.henrique.ms_ticket_manager.application.representation.tickets.GetTicketForSaleByIdDto;
import com.arcilio.henrique.ms_ticket_manager.application.representation.tickets.TicketForSaleByEventDto;
import com.arcilio.henrique.ms_ticket_manager.application.representation.tickets.UpdateTicketDto;
import com.arcilio.henrique.ms_ticket_manager.application.ticket.TicketService;
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
    @PostMapping
    public ResponseEntity<TicketForSale> create(@Valid @RequestBody CreateTicketDto dto){
        TicketForSale ticket = ticketService.createTicketForSale(dto);
        return ResponseEntity.ok(ticket);
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<TicketForSaleByEventDto>> checkForSaleByEventId(@PathVariable String eventId){
        List<TicketForSale> tickets = ticketService.findForSaleByEvent(eventId);
        List<TicketForSaleByEventDto> ticketDtos = TicketMapper.listOfForSaleDto(tickets);
        return ResponseEntity.ok().body(ticketDtos);
    }


    @GetMapping("/{id}")
    public ResponseEntity<GetTicketForSaleByIdDto> getForSaleTicketById(@PathVariable String id){
        TicketForSale ticket = ticketService.findForSaleById(id);
        GetTicketForSaleByIdDto ticketDto = TicketMapper.ticketForSaleDto(ticket);
        return ResponseEntity.ok(ticketDto);
    }

    @GetMapping
    public ResponseEntity<List<TicketForSale>> getAllForSale(){
        List<TicketForSale> tickets = ticketService.findAllForSale();
        return ResponseEntity.ok(tickets);
    }

    @PatchMapping("{/id}")
    public ResponseEntity<Void> updateTicket(@PathVariable String id, @Valid @RequestBody UpdateTicketDto updateDto){
        ticketService.updateTicketForSale(id, updateDto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("{id}/cancel")
    public ResponseEntity<Void> cancelTicketSale(@PathVariable String id){
        ticketService.cancelTicektSale(id);
        return ResponseEntity.noContent().build();
    }

}
