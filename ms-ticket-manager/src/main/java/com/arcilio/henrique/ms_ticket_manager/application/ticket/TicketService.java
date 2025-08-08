package com.arcilio.henrique.ms_ticket_manager.application.ticket;

import com.arcilio.henrique.ms_ticket_manager.application.exception.ticket.ResourceNotFoundException;
import com.arcilio.henrique.ms_ticket_manager.application.representation.tickets.CreateTicketDto;
import com.arcilio.henrique.ms_ticket_manager.application.representation.EventDto;
import com.arcilio.henrique.ms_ticket_manager.application.representation.tickets.GetTicketForSaleByIdDto;
import com.arcilio.henrique.ms_ticket_manager.domain.model.TicketForSale;
import com.arcilio.henrique.ms_ticket_manager.domain.model.UserTicket;
import com.arcilio.henrique.ms_ticket_manager.infra.client.ClientComunicationError;
import com.arcilio.henrique.ms_ticket_manager.infra.client.EventManagerClient;
import com.arcilio.henrique.ms_ticket_manager.infra.repository.TicketForSaleRepository;
import com.arcilio.henrique.ms_ticket_manager.infra.repository.UserTicketRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketForSaleRepository ticketForSaleRepository;
    private final UserTicketRepository userTicketRepository;
    private final EventManagerClient eventManagerClient;

    public TicketForSale createTicketForSale(CreateTicketDto dto) {
        try {
            TicketForSale ticketForSale = new TicketForSale();
            ticketForSale.setBRLTotalAmount(dto.getBrlTotalAmount());
            ticketForSale.setUSDTotalAmount(dto.getBrlTotalAmount() * 5);
            EventDto eventDto = eventManagerClient.getById(dto.getEventId());
            ticketForSale.setEvent(eventDto);
            return ticketForSaleRepository.save(ticketForSale);
        }catch (FeignException e){
            if(e.status() == 404) {
                throw new ResourceNotFoundException("There is no event with such id");
            }
            throw new ClientComunicationError("Unable to communicate with ViaCep client. Try again later");
        }
    }

    public List<TicketForSale> findForSaleByEvent(String eventId) {
        return ticketForSaleRepository.findByEventId(eventId);
    }

    public List<UserTicket> findPurchasedByEvent(String eventId) {
        return userTicketRepository.findByEventId(eventId);
    }

    public TicketForSale findForSaleById(String id) throws ResourceNotFoundException{
        return ticketForSaleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No such ticket is for sale with such id"));
    }
}
