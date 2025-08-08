package com.arcilio.henrique.ms_ticket_manager.application.representation.mapper;

import com.arcilio.henrique.ms_ticket_manager.application.representation.tickets.CheckForSaleTicketDto;
import com.arcilio.henrique.ms_ticket_manager.application.representation.tickets.CheckPurchasedTicketsDto;
import com.arcilio.henrique.ms_ticket_manager.domain.model.TicketForSale;
import com.arcilio.henrique.ms_ticket_manager.domain.model.UserTicket;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.ArrayList;
import java.util.List;

public class TicketMapper {

    public static List<CheckForSaleTicketDto> listOfForSaleDto(List<TicketForSale> tickets) {
        ModelMapper mapper = new ModelMapper();
        List<CheckForSaleTicketDto> ticketDtos = new ArrayList<>();
        mapper.addMappings(new PropertyMap<TicketForSale, CheckForSaleTicketDto>() {
            @Override
            protected void configure() {
                map().setEventName(source.getEvent().getEventName());
            }
        });
        for (TicketForSale ticket : tickets){
            ticketDtos.add(mapper.map(ticket, CheckForSaleTicketDto.class));
        }
        return ticketDtos;
    }
    public static List<CheckPurchasedTicketsDto> listOfPurchasedDto(List<UserTicket> tickets) {
        ModelMapper mapper = new ModelMapper();
        List<CheckPurchasedTicketsDto> ticketDtos = new ArrayList<>();
        for (UserTicket ticket : tickets){
            ticketDtos.add(mapper.map(ticket, CheckPurchasedTicketsDto.class));
        }
        return ticketDtos;
    }
}
