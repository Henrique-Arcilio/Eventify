package com.arcilio.henrique.ms_ticket_manager.application.representation.mapper;

import com.arcilio.henrique.ms_ticket_manager.application.representation.tickets.GetCustomerTicketByIdDto;
import com.arcilio.henrique.ms_ticket_manager.application.representation.tickets.GetTicketByIdDto;
import com.arcilio.henrique.ms_ticket_manager.application.representation.tickets.GetTicketByEventDto;
import com.arcilio.henrique.ms_ticket_manager.application.representation.tickets.GetCustomerTicketsByEventDto;
import com.arcilio.henrique.ms_ticket_manager.domain.model.Ticket;
import com.arcilio.henrique.ms_ticket_manager.domain.model.CustomerTicket;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.ArrayList;
import java.util.List;

public class TicketMapper {

    private static final ModelMapper mapper = new ModelMapper();

    static {
        mapper.addMappings(new PropertyMap<Ticket, GetTicketByEventDto>() {
            @Override
            protected void configure() {
                map().setEventName(source.getEvent().getEventName());
            }
        });
    }
    public static List<GetTicketByEventDto> listOfForSaleDto(List<Ticket> tickets) {
        List<GetTicketByEventDto> ticketDtos = new ArrayList<>();
        for (Ticket ticket : tickets){
            ticketDtos.add(mapper.map(ticket, GetTicketByEventDto.class));
        }
        return ticketDtos;
    }
    public static List<GetCustomerTicketsByEventDto> listOfPurchasedDto(List<CustomerTicket> tickets) {
        List<GetCustomerTicketsByEventDto> ticketDtos = new ArrayList<>();
        for (CustomerTicket ticket : tickets){
            ticketDtos.add(mapper.map(ticket, GetCustomerTicketsByEventDto.class));
        }
        return ticketDtos;
    }

    public static GetTicketByIdDto ticketForSaleDto(Ticket ticket){
        return mapper.map(ticket, GetTicketByIdDto.class);
    }
    public static GetCustomerTicketByIdDto ticketForSaleDto(CustomerTicket customerTicket){
        return mapper.map(customerTicket, GetCustomerTicketByIdDto.class);
    }
}
