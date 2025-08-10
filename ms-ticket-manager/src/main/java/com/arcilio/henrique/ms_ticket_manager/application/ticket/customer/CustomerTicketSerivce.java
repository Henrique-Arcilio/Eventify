package com.arcilio.henrique.ms_ticket_manager.application.ticket.customer;


import com.arcilio.henrique.ms_ticket_manager.application.exception.ticket.ResourceNotFoundException;
import com.arcilio.henrique.ms_ticket_manager.domain.model.CustomerTicket;
import com.arcilio.henrique.ms_ticket_manager.domain.model.Ticket;
import com.arcilio.henrique.ms_ticket_manager.domain.model.TicketStatus;
import com.arcilio.henrique.ms_ticket_manager.domain.model.User;
import com.arcilio.henrique.ms_ticket_manager.infra.repository.CustomerTicketRepository;
import com.arcilio.henrique.ms_ticket_manager.infra.repository.TicketForSaleRepository;
import com.arcilio.henrique.ms_ticket_manager.infra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerTicketSerivce {

    private final CustomerTicketRepository customerTicketRepository;
    private final TicketForSaleRepository ticketForSaleRepository;
    private final UserRepository userRepository;

    public CustomerTicket create(String ticketId, UserDetails userDetails) {
        Optional<Ticket> ticketForSaleOp = ticketForSaleRepository.findById(ticketId);
        Ticket ticketForSale = ticketForSaleOp
                .orElseThrow(
                        () -> new ResourceNotFoundException("No ticket for sale with the given id"));

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

        return customerTicketRepository.save(ticket);
    }

    public CustomerTicket findById(String id) throws ResourceNotFoundException {
        return customerTicketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No ticket for sale found with the given id"));
    }

    public List<CustomerTicket> findByEventId(String eventId) {
        return customerTicketRepository.findByEventIdAndStatus(eventId, TicketStatus.ACTIVE);
    }

    public void cancelUserTicket(String id) {
        Optional<CustomerTicket> ticketOp = customerTicketRepository.findById(id);
        CustomerTicket ticket = ticketOp.orElseThrow(() -> new ResourceNotFoundException("Found no ticket with the given id"));
        ticket.setStatus(TicketStatus.CANCELLED);
        customerTicketRepository.save(ticket);
    }

}
