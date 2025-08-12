package com.arcilio.henrique.ms_event_manager.application;

import com.arcilio.henrique.ms_event_manager.application.exception.CancelledEventException;
import com.arcilio.henrique.ms_event_manager.application.exception.ResourceNotFoundException;
import com.arcilio.henrique.ms_event_manager.application.representation.ViaCepAdressDto;
import com.arcilio.henrique.ms_event_manager.domain.model.Event;
import com.arcilio.henrique.ms_event_manager.domain.model.EventStatus;
import com.arcilio.henrique.ms_event_manager.infra.clients.exception.CepNotFoundException;
import com.arcilio.henrique.ms_event_manager.infra.clients.exception.ClientComunicationError;
import com.arcilio.henrique.ms_event_manager.infra.clients.viacep.ViaCepClient;
import com.arcilio.henrique.ms_event_manager.infra.repository.EventRepository;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class EventServiceTest {

    @MockitoBean
    private ViaCepClient viaCepClient;

    @MockitoBean
    private EventRepository eventRepository;

    @Autowired
    private EventService eventService;

    @BeforeEach
    void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Should create a new event Successfully")

    void createEventSucess() {
        Event event = new Event("Test event", LocalDateTime.now().plusDays(1), "01001-000");

        ViaCepAdressDto addressDto = new ViaCepAdressDto();
        addressDto.setBairro("Sé");
        addressDto.setLocalidade("São Paulo");
        addressDto.setLogradouro("Praça da Sé");
        addressDto.setUf("SP");
        addressDto.setErro(false);

        when(viaCepClient.getAdressInformation("01001-000")).thenReturn(addressDto);
        when(eventRepository.save(any(Event.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Event result = eventService.createEvent(event);

        assertNotNull(result);
        assertEquals(EventStatus.ACTIVE, result.getStatus());
        assertEquals("Sé", result.getBairro());
        assertEquals("São Paulo", result.getCidade());
        assertEquals("Praça da Sé", result.getLogradouro());
        assertEquals("SP", result.getUf());

        verify(viaCepClient).getAdressInformation("01001-000");
        verify(eventRepository).save(any(Event.class));
    }

    @Test
    @DisplayName("Should throw ClientComunicationError when ViaCepClient throws FeignException")
    void createEventFeignException() {
        Event event = new Event("Test event", LocalDateTime.now().plusDays(1), "01001-000");

        when(viaCepClient.getAdressInformation("01001-000")).thenThrow(FeignException.class);

        ClientComunicationError exception = assertThrows(ClientComunicationError.class, () -> {
            eventService.createEvent(event);
        });

        assertEquals("Unable to communicate with ViaCep client. Try again later", exception.getMessage());

        verify(viaCepClient).getAdressInformation("01001-000");
    }


    @Test
    @DisplayName("Failed to create because Cep doesn't exist")
    void createEventCepNotExist() {
        Event event = new Event("Test event", LocalDateTime.now().plusDays(1), "00000000");

        ViaCepAdressDto addressDto = new ViaCepAdressDto();
        addressDto.setErro(true);

        when(viaCepClient.getAdressInformation("00000000")).thenReturn(addressDto);

        CepNotFoundException exception = assertThrows(CepNotFoundException.class, () -> {
            eventService.createEvent(event);
        });

        assertEquals("The provided CEP doesn't exist", exception.getMessage());

        verify(viaCepClient).getAdressInformation("00000000");
    }


    @Test
    void findAll() {
    }

    @Test
    void update() {
    }

    @Test
    void syncEventUpdate() {
    }

    @Test
    void cancel() {
    }
}