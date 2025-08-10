package com.arcilio.henrique.ms_event_manager.application;

import com.arcilio.henrique.ms_event_manager.application.exception.CancelledEventException;
import com.arcilio.henrique.ms_event_manager.application.exception.ResourceNotFoundException;
import com.arcilio.henrique.ms_event_manager.application.representation.CheckTicketDto;
import com.arcilio.henrique.ms_event_manager.application.representation.UpdateEventDto;
import com.arcilio.henrique.ms_event_manager.domain.model.EventStatus;
import com.arcilio.henrique.ms_event_manager.infra.clients.exception.ActiveTicketException;
import com.arcilio.henrique.ms_event_manager.infra.clients.exception.ClientComunicationError;
import com.arcilio.henrique.ms_event_manager.infra.clients.exception.CepNotFoundException;
import com.arcilio.henrique.ms_event_manager.application.representation.ViaCepAdressDto;
import com.arcilio.henrique.ms_event_manager.domain.model.Event;
import com.arcilio.henrique.ms_event_manager.infra.clients.ticket.TicketManagerClient;
import com.arcilio.henrique.ms_event_manager.infra.clients.viacep.ViaCepClient;
import com.arcilio.henrique.ms_event_manager.infra.repository.EventRepository;
import feign.FeignException;
import feign.RetryableException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final ViaCepClient viaCepClient;
    private final TicketManagerClient ticketManagerClient;

    public Event createEvent(Event event){
        try {
            event.setStatus(EventStatus.ACTIVE);
            return eventRepository.save(  insertViaCepValues(event));

        }catch (FeignException e){
            throw new ClientComunicationError("Unable to communicate with ViaCep client. Try again later");
        }
    }

    public Event findById(String id){
        Optional<Event> eventOp = eventRepository.findById(id);
        Event event = eventOp.orElseThrow(() -> new ResourceNotFoundException("No event found with such id"));
        if(event.getStatus() == EventStatus.CANCELLED){
            throw new CancelledEventException("The event with the provided id has been cancelled");
        }
        return event;
    }

    public Page<Event> findAll(Pageable pageable){
        return eventRepository.findAll(pageable);
    }

    public void update(String id, UpdateEventDto updateDto){
        Optional<Event> eventOp = eventRepository.findById(id);
        Event event = eventOp
                .orElseThrow(() ->
                        new ResourceNotFoundException("There is no event with such id"));
        insertUpdateValues(updateDto, event);
        syncEventUpdate(id);
        eventRepository.save(event);
    }

    public void syncEventUpdate(String eventId){
        ticketManagerClient.syncEventUpdates(eventId);
    }

    private Event insertUpdateValues(UpdateEventDto updateDto, Event event){

        if(updateDto.getEventName() != null && !updateDto.getEventName().isBlank()){
            event.setEventName(updateDto.getEventName().trim());
        }
        if(updateDto.getDateTime() != null){
            event.setDateTime(updateDto.getDateTime());
        }
        if(updateDto.getCep() != null && !updateDto.getCep().equals(event.getCep())){
           event.setCep(updateDto.getCep());
           insertViaCepValues(event);
        }
        return event;
    }
    private Event insertViaCepValues(Event event) {
        ViaCepAdressDto addressInformation = viaCepClient.getAdressInformation(event.getCep());
        if(addressInformation.isErro()){
            throw new CepNotFoundException("The CEP provided doesn't exist");
        }
        event.setBairro(addressInformation.getBairro());
        event.setCidade(addressInformation.getLocalidade());
        event.setLogradouro(addressInformation.getLogradouro());
        event.setUf(addressInformation.getUf());
        return event;
    };

    public void cancel(String id) {
        Optional<Event> eventOp = eventRepository.findById(id);
        Event event = eventOp.orElseThrow(
                () -> new ResourceNotFoundException("No event found with the given id"));

        try{
            List<CheckTicketDto> forSale = ticketManagerClient.checkTicketsByEventId(id);
            List<CheckTicketDto> purchased = ticketManagerClient.checkCustomerTicketsByEventId(id);
            checkTickets(forSale, purchased);
        }catch (FeignException e){
            throw new ClientComunicationError("Unable to communicate with ms-ticket-manager client. Try again later");
        }

        event.setStatus(EventStatus.CANCELLED);
        eventRepository.save(event);
    }

    private void checkTickets(List<CheckTicketDto> forSale, List<CheckTicketDto> purchased){
        if(!forSale.isEmpty()){
            throw new ActiveTicketException("There are still tickets on sale for this event. please speak to the ticket administrator ", forSale);
        }
        if(!purchased.isEmpty()){
            throw new ActiveTicketException("It is not possible to delete this event. " +
                    "Tickets have already been sold for it.", purchased);
        }
    }

}
