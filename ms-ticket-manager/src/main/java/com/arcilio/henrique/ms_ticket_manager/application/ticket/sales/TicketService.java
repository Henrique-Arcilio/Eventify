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
                throw new ResourceNotFoundException("There is no event with such id");
            }else if(e.status() == 409){
                throw new CancelledEventException("The event with the provided id has been cancelled");
            }else{
                throw new ClientComunicationError("Unable to comunicate with ms-event-client. Try again later");
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

    public List<Ticket> findAll() {
        return ticketForSaleRepository.findAll();
    }

    public void updatePrice(String id, UpdateTicketDto updateDto) {
        Optional<Ticket> ticketOp = ticketForSaleRepository.findById(id);
        Ticket ticket = ticketOp.orElseThrow(() -> new ResourceNotFoundException("No ticket found with given id"));
        ticketForSaleRepository.save( updateValues(ticket, updateDto));
    }

    Ticket updateValues(Ticket ticket, UpdateTicketDto updateDto){
        if(updateDto.getBrlTotalAmount() != null){
            ticket.setBrlTotalAmount(updateDto.getBrlTotalAmount());
            ticket.setUsdTotalAmount(ticket.getBrlTotalAmount() * 5);
        }
        return ticket;
    }

    public void cancel(String id) {
        Optional<Ticket> ticketOp = ticketForSaleRepository.findById(id);
        Ticket ticket = ticketOp.orElseThrow(() -> new ResourceNotFoundException("No found ticket with the given id"));
        ticket.setStatus(TicketStatus.CANCELLED);
        ticketForSaleRepository.save(ticket);
    }

}
