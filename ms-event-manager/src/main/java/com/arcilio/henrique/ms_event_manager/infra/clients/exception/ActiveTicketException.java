package com.arcilio.henrique.ms_event_manager.infra.clients.exception;

import com.arcilio.henrique.ms_event_manager.application.representation.CheckTicketDto;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ActiveTicketException extends RuntimeException {
    private List<String> activeTickets;
    public ActiveTicketException(String message, List<CheckTicketDto> dtoTickets) {
        super(message);
        activeTickets = new ArrayList<>();
        for(CheckTicketDto ticket : dtoTickets){
          activeTickets.add(ticket.getId());
        }
    }

}
