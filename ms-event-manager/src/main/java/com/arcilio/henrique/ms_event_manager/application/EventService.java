package com.arcilio.henrique.ms_event_manager.application;

import com.arcilio.henrique.ms_event_manager.application.representation.ViaCepAdressDto;
import com.arcilio.henrique.ms_event_manager.domain.model.Event;
import com.arcilio.henrique.ms_event_manager.infra.clients.ViaCepClient;
import com.arcilio.henrique.ms_event_manager.infra.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final ViaCepClient viaCepClient;

    public Event createEvent(Event event){
        ViaCepAdressDto adressInformation = viaCepClient.getAdressInformation(event.getCep());

        event.setBairro(adressInformation.getBairro());
        event.setCidade(adressInformation.getLocalidade());
        event.setLogradouro(adressInformation.getLogradouro());
        event.setUf(adressInformation.getUf());
        return eventRepository.save(event);
    }
}
