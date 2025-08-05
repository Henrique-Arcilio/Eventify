package com.arcilio.henrique.ms_event_manager.application;

import com.arcilio.henrique.ms_event_manager.application.exception.ResourceNotFoundException;
import com.arcilio.henrique.ms_event_manager.infra.clients.exception.ClientComunicationError;
import com.arcilio.henrique.ms_event_manager.infra.clients.exception.CepNotFoundException;
import com.arcilio.henrique.ms_event_manager.application.representation.ViaCepAdressDto;
import com.arcilio.henrique.ms_event_manager.domain.model.Event;
import com.arcilio.henrique.ms_event_manager.infra.clients.viacep.ViaCepClient;
import com.arcilio.henrique.ms_event_manager.infra.repository.EventRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final ViaCepClient viaCepClient;

    public Event createEvent(Event event){
        try {
            ViaCepAdressDto addressInformation = viaCepClient.getAdressInformation(event.getCep());
            if(addressInformation.isErro()){
                throw new CepNotFoundException("The CEP provided doesn't exist");
            }

            event.setBairro(addressInformation.getBairro());
            event.setCidade(addressInformation.getLocalidade());
            event.setLogradouro(addressInformation.getLogradouro());
            event.setUf(addressInformation.getUf());

            return eventRepository.save(event);

        }catch (FeignException.FeignClientException e){
            throw new ClientComunicationError("Unable to communicate with ViaCep client. Try again later");
        }
    }

    public Event findById(String id){
        Optional<Event> event = eventRepository.findById(id);
        return event.orElseThrow(() -> new ResourceNotFoundException("No event found with such id"));
    }
}
