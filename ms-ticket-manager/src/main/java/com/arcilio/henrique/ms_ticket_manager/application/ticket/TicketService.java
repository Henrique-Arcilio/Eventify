package com.arcilio.henrique.ms_ticket_manager.application.ticket;

import com.arcilio.henrique.ms_ticket_manager.application.representation.CreateTicketDto;
import com.arcilio.henrique.ms_ticket_manager.application.representation.EventDto;
import com.arcilio.henrique.ms_ticket_manager.domain.model.TicketForSale;
import com.arcilio.henrique.ms_ticket_manager.infra.client.EventManagerClient;
import com.arcilio.henrique.ms_ticket_manager.infra.repository.TicketForSaleRepository;
import com.arcilio.henrique.ms_ticket_manager.infra.repository.UserTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketForSaleRepository ticketForSaleRepository;
    private final UserTicketRepository userTicketRepository;
    private final EventManagerClient eventManagerClient;

    public TicketForSale createTicketForSale(CreateTicketDto dto) {
        TicketForSale ticketForSale = new TicketForSale();
        ticketForSale.setBRLTotalAmount(dto.getBrlTotalAmount());
        ticketForSale.setUSDTotalAmount(dto.getBrlTotalAmount() * 5);
        EventDto eventDto = eventManagerClient.getById(dto.getEventId());
        ticketForSale.setEvent(eventDto);
        return ticketForSaleRepository.save(ticketForSale);
    }
}
