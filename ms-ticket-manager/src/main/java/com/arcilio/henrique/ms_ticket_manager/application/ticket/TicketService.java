package com.arcilio.henrique.ms_ticket_manager.application.ticket;

import com.arcilio.henrique.ms_ticket_manager.application.exception.ticket.ResourceNotFoundException;
import com.arcilio.henrique.ms_ticket_manager.application.representation.tickets.CreateTicketDto;
import com.arcilio.henrique.ms_ticket_manager.application.representation.EventDto;
import com.arcilio.henrique.ms_ticket_manager.application.representation.tickets.UpdateTicketDto;
import com.arcilio.henrique.ms_ticket_manager.domain.model.Ticket;
import com.arcilio.henrique.ms_ticket_manager.domain.model.TicketStatus;
import com.arcilio.henrique.ms_ticket_manager.domain.model.User;
import com.arcilio.henrique.ms_ticket_manager.domain.model.CustomerTicket;
import com.arcilio.henrique.ms_ticket_manager.infra.client.ClientComunicationError;
import com.arcilio.henrique.ms_ticket_manager.infra.client.EventManagerClient;
import com.arcilio.henrique.ms_ticket_manager.infra.repository.TicketForSaleRepository;
import com.arcilio.henrique.ms_ticket_manager.infra.repository.UserRepository;
import com.arcilio.henrique.ms_ticket_manager.infra.repository.UserTicketRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketForSaleRepository ticketForSaleRepository;
    private final UserTicketRepository userTicketRepository;
    private final EventManagerClient eventManagerClient;
    private final UserRepository userRepository;

    public Ticket createTicketForSale(CreateTicketDto dto) {
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
            }
            throw new ClientComunicationError("Unable to communicate with ViaCep client. Try again later");
        }
    }

    public CustomerTicket createUserTicket(String ticketId, UserDetails userDetails) {
        Optional<Ticket> ticketForSaleOp = ticketForSaleRepository.findById(ticketId);
        Ticket ticketForSale = ticketForSaleOp
                .orElseThrow(
                () -> new ResourceNotFoundException("There is not ticket for sale with the given id"));

        User user = userRepository.findByUsername(userDetails.getUsername());
        CustomerTicket ticket = new CustomerTicket();

        ticket.setCpf(user.getCpf());
        ticket.setUserId(user.getId());
        ticket.setEventId(ticketForSale.getEvent().getId());
        ticket.setPurchasedTicketId(ticketForSale.getId());
        ticket.setCustomerName(user.getFullname());
        ticket.setCustomerMail(user.getUsername());
        ticket.setBrlTotalAmount(ticketForSale.getBrlTotalAmount());
        ticket.setUsdTotalAmount(ticketForSale.getUsdTotalAmount());
        ticket.setStatus(TicketStatus.ACTIVE);

        return userTicketRepository.save(ticket);
    }

    public List<Ticket> findForSaleByEvent(String eventId) {
        return ticketForSaleRepository.findByEventIdAndStatus(eventId, TicketStatus.ACTIVE);
    }

    public List<CustomerTicket> findPurchasedByEvent(String eventId) {
        return userTicketRepository.findByEventIdAndStatus(eventId, TicketStatus.ACTIVE);
    }

    public Ticket findForSaleById(String id) throws ResourceNotFoundException{
        return ticketForSaleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No ticket for sale found with the given id"));
    }

    public CustomerTicket findUserTicketById(String id) throws ResourceNotFoundException{
        return userTicketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No ticket for sale found with the given id"));
    }

    public List<Ticket> findAllForSale() {
        return ticketForSaleRepository.findAll();
    }

    public void updateTicketForSale(String id, UpdateTicketDto updateDto) {
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

    public void cancelTicektSale(String id) {
        Optional<Ticket> ticketOp = ticketForSaleRepository.findById(id);
        Ticket ticket = ticketOp.orElseThrow(() -> new ResourceNotFoundException("No found ticket with the given id"));
        ticket.setStatus(TicketStatus.CANCELLED);
        ticketForSaleRepository.save(ticket);
    }
    public void cancelUserTicket(String id) {
        Optional<CustomerTicket> ticketOp = userTicketRepository.findById(id);
        CustomerTicket ticket = ticketOp.orElseThrow(() -> new ResourceNotFoundException("No found ticket with the given id"));
        ticket.setStatus(TicketStatus.CANCELLED);
        userTicketRepository.save(ticket);
    }
}
