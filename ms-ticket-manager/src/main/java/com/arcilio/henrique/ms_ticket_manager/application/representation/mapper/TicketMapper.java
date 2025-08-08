package com.arcilio.henrique.ms_ticket_manager.application.representation.mapper;

import com.arcilio.henrique.ms_ticket_manager.application.representation.tickets.GetUserTicketByIdDto;
import com.arcilio.henrique.ms_ticket_manager.application.representation.tickets.GetTicketForSaleByIdDto;
import com.arcilio.henrique.ms_ticket_manager.application.representation.tickets.TicketForSaleByEventDto;
import com.arcilio.henrique.ms_ticket_manager.application.representation.tickets.PurchasedTicketsByEventDto;
import com.arcilio.henrique.ms_ticket_manager.domain.model.TicketForSale;
import com.arcilio.henrique.ms_ticket_manager.domain.model.UserTicket;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.ArrayList;
import java.util.List;

public class TicketMapper {

    private static final ModelMapper mapper = new ModelMapper();

    public static List<TicketForSaleByEventDto> listOfForSaleDto(List<TicketForSale> tickets) {
        List<TicketForSaleByEventDto> ticketDtos = new ArrayList<>();
        mapper.addMappings(new PropertyMap<TicketForSale, TicketForSaleByEventDto>() {
            @Override
            protected void configure() {
                map().setEventName(source.getEvent().getEventName());
            }
        });
        for (TicketForSale ticket : tickets){
            ticketDtos.add(mapper.map(ticket, TicketForSaleByEventDto.class));
        }
        return ticketDtos;
    }
    public static List<PurchasedTicketsByEventDto> listOfPurchasedDto(List<UserTicket> tickets) {
        List<PurchasedTicketsByEventDto> ticketDtos = new ArrayList<>();
        for (UserTicket ticket : tickets){
            ticketDtos.add(mapper.map(ticket, PurchasedTicketsByEventDto.class));
        }
        return ticketDtos;
    }

    public static GetTicketForSaleByIdDto ticketForSaleDto(TicketForSale ticketForSale){
        return mapper.map(ticketForSale, GetTicketForSaleByIdDto.class);
    }
    public static GetUserTicketByIdDto ticketForSaleDto(UserTicket userTicket){
        return mapper.map(userTicket, GetUserTicketByIdDto.class);
    }
}
