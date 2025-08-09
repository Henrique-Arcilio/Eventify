package com.arcilio.henrique.ms_ticket_manager.application.ticket;

import com.arcilio.henrique.ms_ticket_manager.application.exception.ticket.ResourceNotFoundException;
import com.arcilio.henrique.ms_ticket_manager.application.representation.tickets.CreateTicketDto;
import com.arcilio.henrique.ms_ticket_manager.application.representation.EventDto;
import com.arcilio.henrique.ms_ticket_manager.application.representation.tickets.UpdateTicketDto;
import com.arcilio.henrique.ms_ticket_manager.domain.model.TicketForSale;
import com.arcilio.henrique.ms_ticket_manager.domain.model.TicketStatus;
import com.arcilio.henrique.ms_ticket_manager.domain.model.User;
import com.arcilio.henrique.ms_ticket_manager.domain.model.UserTicket;
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

    public TicketForSale createTicketForSale(CreateTicketDto dto) {
        try {
            TicketForSale ticketForSale = new TicketForSale();
            ticketForSale.setBRLTotalAmount(dto.getBrlTotalAmount());
            ticketForSale.setUSDTotalAmount(dto.getBrlTotalAmount() * 5);
            EventDto eventDto = eventManagerClient.getById(dto.getEventId());
            ticketForSale.setEvent(eventDto);
            ticketForSale.setStatus(TicketStatus.ACTIVE);
            return ticketForSaleRepository.save(ticketForSale);
        }catch (FeignException e){
            if(e.status() == 404) {
                throw new ResourceNotFoundException("There is no event with such id");
            }
            throw new ClientComunicationError("Unable to communicate with ViaCep client. Try again later");
        }
    }

    public UserTicket createUserTicket(String ticketId, UserDetails userDetails) {
        Optional<TicketForSale> ticketForSaleOp = ticketForSaleRepository.findById(ticketId);
        TicketForSale ticketForSale = ticketForSaleOp
                .orElseThrow(
                () -> new ResourceNotFoundException("There is not ticket for sale with the given id"));

        User user = userRepository.findByUsername(userDetails.getUsername());
        UserTicket ticket = new UserTicket();

        ticket.setCpf(user.getCpf());
        ticket.setUserId(user.getId());
        ticket.setEventId(ticketForSale.getEvent().getId());
        ticket.setPurchasedTicketId(ticketForSale.getId());
        ticket.setCostumerName(user.getFullname());
        ticket.setCostumerMail(user.getUsername());
        ticket.setBRLTotalAmount(ticketForSale.getBRLTotalAmount());
        ticket.setUSDTotalAmount(ticketForSale.getUSDTotalAmount());
        ticket.setStatus(TicketStatus.ACTIVE);

        return userTicketRepository.save(ticket);
    }

    public List<TicketForSale> findForSaleByEvent(String eventId) {
        return ticketForSaleRepository.findByEventIdAndStatus(eventId, TicketStatus.ACTIVE);
    }

    public List<UserTicket> findPurchasedByEvent(String eventId) {
        return userTicketRepository.findByEventIdAndStatus(eventId, TicketStatus.ACTIVE);
    }

    public TicketForSale findForSaleById(String id) throws ResourceNotFoundException{
        return ticketForSaleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No ticket for sale found with the given id"));
    }

    public UserTicket findUserTicketById(String id) throws ResourceNotFoundException{
        return userTicketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No ticket for sale found with the given id"));
    }

    public List<TicketForSale> findAllForSale() {
        return ticketForSaleRepository.findAll();
    }

    public void updateTicketForSale(String id, UpdateTicketDto updateDto) {
        Optional<TicketForSale> ticketOp = ticketForSaleRepository.findById(id);
        TicketForSale ticket = ticketOp.orElseThrow(() -> new ResourceNotFoundException("No ticket found with given id"));
        ticketForSaleRepository.save( updateValues(ticket, updateDto));
    }

    TicketForSale updateValues(TicketForSale ticketForSale, UpdateTicketDto updateDto){
        if(updateDto.getBrlTotalAmount() != null){
            ticketForSale.setBRLTotalAmount(updateDto.getBrlTotalAmount());
            ticketForSale.setUSDTotalAmount(ticketForSale.getBRLTotalAmount() * 5);
        }
        return ticketForSale;
    }

    public void cancelTicektSale(String id) {
        Optional<TicketForSale> ticketOp = ticketForSaleRepository.findById(id);
        TicketForSale ticket = ticketOp.orElseThrow(() -> new ResourceNotFoundException("No found ticket with the given id"));
        ticket.setStatus(TicketStatus.CANCELLED);
        ticketForSaleRepository.save(ticket);
    }
}
