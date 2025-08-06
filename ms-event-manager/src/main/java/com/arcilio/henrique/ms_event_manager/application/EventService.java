package com.arcilio.henrique.ms_event_manager.application;

import com.arcilio.henrique.ms_event_manager.application.exception.ResourceNotFoundException;
import com.arcilio.henrique.ms_event_manager.application.representation.UpdateEventDto;
import com.arcilio.henrique.ms_event_manager.infra.clients.exception.ClientComunicationError;
import com.arcilio.henrique.ms_event_manager.infra.clients.exception.CepNotFoundException;
import com.arcilio.henrique.ms_event_manager.application.representation.ViaCepAdressDto;
import com.arcilio.henrique.ms_event_manager.domain.model.Event;
import com.arcilio.henrique.ms_event_manager.infra.clients.viacep.ViaCepClient;
import com.arcilio.henrique.ms_event_manager.infra.repository.EventRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final ViaCepClient viaCepClient;

    public Event createEvent(Event event){
        try {
            return eventRepository.save(  insertViaCepValues(event));

        }catch (FeignException.FeignClientException e){
            throw new ClientComunicationError("Unable to communicate with ViaCep client. Try again later");
        }
    }

    public Event findById(String id){
        Optional<Event> event = eventRepository.findById(id);
        return event.orElseThrow(() -> new ResourceNotFoundException("No event found with such id"));
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
        eventRepository.save(event);
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

}
