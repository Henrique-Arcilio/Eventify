package com.arcilio.henrique.ms_ticket_manager.application.ticket.sales;

import com.arcilio.henrique.ms_ticket_manager.application.exception.event.CancelledEventException;
import com.arcilio.henrique.ms_ticket_manager.application.exception.ticket.ResourceNotFoundException;
import com.arcilio.henrique.ms_ticket_manager.application.representation.EventDto;
import com.arcilio.henrique.ms_ticket_manager.application.representation.tickets.CreateTicketDto;
import com.arcilio.henrique.ms_ticket_manager.application.representation.tickets.UpdateTicketDto;
import com.arcilio.henrique.ms_ticket_manager.domain.model.Ticket;
import com.arcilio.henrique.ms_ticket_manager.domain.model.TicketStatus;
import com.arcilio.henrique.ms_ticket_manager.infra.client.ClientComunicationError;
import com.arcilio.henrique.ms_ticket_manager.infra.client.EventManagerClient;
import com.arcilio.henrique.ms_ticket_manager.infra.repository.TicketForSaleRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketForSaleRepository ticketForSaleRepository;
    private final EventManagerClient eventManagerClient;

    public Ticket create(CreateTicketDto dto) {
        try {
            Ticket ticket = new Ticket();
            ticket.setBrlTotalAmount(dto.getBrlTotalAmount());
            ticket.setUsdTotalAmount(dto.getBrlTotalAmount() * 5);
            EventDto eventDto = eventManagerClient.getById(dto.getEventId());
            ticket.setEvent(eventDto);
            ticket.setStatus(TicketStatus.ACTIVE);
            return ticketForSaleRepository.save(ticket);
        }catch (FeignException e){
            if(e.status() == 404) {
                throw new ResourceNotFoundException("No event found with the given id.");
            }else if(e.status() == 409){
                throw new CancelledEventException("The event with the provided id has been cancelled");
            }else{
                throw new ClientComunicationError("Unable to communicate with ms-event-client. Try again later");
            }
        }
    }

    public List<Ticket> findByEventId(String eventId) {
        return ticketForSaleRepository.findByEventIdAndStatus(eventId, TicketStatus.ACTIVE);
    }

    public Ticket findById(String id) throws ResourceNotFoundException{
        return ticketForSaleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No ticket for sale found with the given id"));
    }

    public Page<Ticket> findAll(Pageable pageable) {
        return ticketForSaleRepository.findAll(pageable);
    }

    public void updatePrice(String id, UpdateTicketDto updateDto) {
        Optional<Ticket> ticketOp = ticketForSaleRepository.findById(id);
        Ticket ticket = ticketOp.orElseThrow(() -> new ResourceNotFoundException("No ticket found with the given id"));
        ticketForSaleRepository.save( updateValues(ticket, updateDto));
    }

    Ticket updateValues(Ticket ticket, UpdateTicketDto updateDto){
        if(updateDto.getBrlTotalAmount() != null){
            ticket.setBrlTotalAmount(updateDto.getBrlTotalAmount());
            ticket.setUsdTotalAmount(ticket.getBrlTotalAmount() * 5);
        }
        return ticket;
    }

    void syncEventUpdates(String eventId){
        List<Ticket> tickets = findByEventId(eventId);
        EventDto eventDto = eventManagerClient.getById(eventId);
        for(Ticket ticket : tickets){
            ticket.setEvent(eventDto);
        }
        ticketForSaleRepository.saveAll(tickets);
    }

    public void cancel(String id) {
        Optional<Ticket> ticketOp = ticketForSaleRepository.findById(id);
        Ticket ticket = ticketOp.orElseThrow(() -> new ResourceNotFoundException("No found ticket with the given id"));
        ticket.setStatus(TicketStatus.CANCELLED);
        ticketForSaleRepository.save(ticket);
    }

}
