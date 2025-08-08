package com.arcilio.henrique.ms_ticket_manager.application.representation.mapper;

import com.arcilio.henrique.ms_ticket_manager.application.representation.CheckForSaleTicketDto;
import com.arcilio.henrique.ms_ticket_manager.domain.model.TicketForSale;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.ArrayList;
import java.util.List;

public class TicketMapper {

    public static List<CheckForSaleTicketDto> toCheckEventTicket(List<TicketForSale> tickets) {
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
}
