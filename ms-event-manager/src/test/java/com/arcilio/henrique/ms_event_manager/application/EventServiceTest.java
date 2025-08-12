package com.arcilio.henrique.ms_event_manager.application;

import com.arcilio.henrique.ms_event_manager.application.exception.CancelledEventException;
import com.arcilio.henrique.ms_event_manager.application.exception.ResourceNotFoundException;
import com.arcilio.henrique.ms_event_manager.application.representation.CheckTicketDto;
import com.arcilio.henrique.ms_event_manager.application.representation.UpdateEventDto;
import com.arcilio.henrique.ms_event_manager.application.representation.ViaCepAdressDto;
import com.arcilio.henrique.ms_event_manager.domain.model.Event;
import com.arcilio.henrique.ms_event_manager.domain.model.EventStatus;
import com.arcilio.henrique.ms_event_manager.infra.clients.exception.ActiveTicketException;
import com.arcilio.henrique.ms_event_manager.infra.clients.exception.CepNotFoundException;
import com.arcilio.henrique.ms_event_manager.infra.clients.exception.ClientComunicationError;
import com.arcilio.henrique.ms_event_manager.infra.clients.ticket.TicketManagerClient;
import com.arcilio.henrique.ms_event_manager.infra.clients.viacep.ViaCepClient;
import com.arcilio.henrique.ms_event_manager.infra.repository.EventRepository;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    @MockitoBean
    private TicketManagerClient ticketManagerClient;


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
    @DisplayName("Should return event when found and active")
    void findByIdSucess() {
        Event event = new Event("Event 1", LocalDateTime.now().plusDays(1), "01001-000");
        event.setStatus(EventStatus.ACTIVE);
        event.setId("123");

        when(eventRepository.findById("123")).thenReturn(Optional.of(event));

        Event result = eventService.findById("123");

        assertNotNull(result);
        assertEquals("123", result.getId());
        assertEquals(EventStatus.ACTIVE, result.getStatus());
        verify(eventRepository).findById("123");
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when event doesn't exist by id")
    void findByIdNotFound() {
        when(eventRepository.findById("0")).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            eventService.findById("0");
        });

        assertEquals("No event found with the given id", ex.getMessage());
        verify(eventRepository).findById("0");
    }


    @Test
    @DisplayName("Should throw CancelledEventException when event is cancelled")
    void findByIdCancelledEvent() {
        Event event = new Event("Event Cancelled", LocalDateTime.now().plusDays(1), "01001-000");
        event.setStatus(EventStatus.CANCELLED);
        event.setId("1");

        when(eventRepository.findById("1")).thenReturn(Optional.of(event));

        CancelledEventException ex = assertThrows(CancelledEventException.class, () -> {
            eventService.findById("1");
        });

        assertEquals("The event with the given id has been cancelled", ex.getMessage());
        verify(eventRepository).findById("1");
    }



    @Test
    void findAll() {
        Pageable pageable = Pageable.ofSize(5);
        Page<Event> page = Page.empty(pageable);

        when(eventRepository.findAll(pageable)).thenReturn(page);

        Page<Event> result = eventService.findAll(pageable);

        assertNotNull(result);
        verify(eventRepository).findAll(pageable);
    }




    @Test
    @DisplayName("Should update event successfully")
    void updateSuccess() {
        String eventId = "123";
        UpdateEventDto updateDto = new UpdateEventDto();
        updateDto.setEventName("Updated name");
        updateDto.setDateTime(LocalDateTime.now().plusDays(5));
        updateDto.setCep("01001-000");

        Event event = new Event("Old name", LocalDateTime.now().plusDays(1), "99999-999");
        event.setId(eventId);

        ViaCepAdressDto addressDto = new ViaCepAdressDto();
        addressDto.setErro(false);
        addressDto.setBairro("Sé");
        addressDto.setLocalidade("São Paulo");
        addressDto.setLogradouro("Praça da Sé");
        addressDto.setUf("SP");

        when(viaCepClient.getAdressInformation("01001-000")).thenReturn(addressDto);
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(eventRepository.save(event)).thenReturn(event);

        eventService.update(eventId, updateDto);

        assertEquals("Updated name", event.getEventName());
        assertEquals("01001-000", event.getCep());
        assertEquals(updateDto.getDateTime(), event.getDateTime());

        verify(eventRepository).findById(eventId);
        verify(eventRepository).save(event);
    }


    @Test
    @DisplayName("Should throw ActiveTicketException when tickets are still for sale")
    void cancelActiveTicketsForSale() {
        String eventId = "123";
        Event event = new Event("Event", LocalDateTime.now().plusDays(1), "01001-000");
        event.setStatus(EventStatus.ACTIVE);
        event.setId(eventId);

        List<CheckTicketDto> ticketsForSale = new ArrayList<>();
        ticketsForSale.add(new CheckTicketDto());
        List<CheckTicketDto> ticketsPurchased = List.of();

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(ticketManagerClient.checkTicketsByEventId(eventId)).thenReturn(ticketsForSale);
        when(ticketManagerClient.checkCustomerTicketsByEventId(eventId)).thenReturn(ticketsPurchased);

        ActiveTicketException ex = assertThrows(ActiveTicketException.class, () -> {
            eventService.cancel(eventId);
        });

        assertTrue(ex.getMessage().contains("There are still tickets on sale"));
        verify(eventRepository).findById(eventId);
        verify(ticketManagerClient).checkTicketsByEventId(eventId);
        verify(ticketManagerClient).checkCustomerTicketsByEventId(eventId);
    }

    @Test
    @DisplayName("Should throw ActiveTicketException when tickets have already been sold")
    void cancelTicketsAlreadySold() {
        String eventId = "123";
        Event event = new Event("Event", LocalDateTime.now().plusDays(1), "01001-000");
        event.setStatus(EventStatus.ACTIVE);
        event.setId(eventId);

        List<CheckTicketDto> ticketsForSale = new ArrayList<>();
        List<CheckTicketDto> ticketsPurchased = List.of(new CheckTicketDto());

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(ticketManagerClient.checkTicketsByEventId(eventId)).thenReturn(ticketsForSale);
        when(ticketManagerClient.checkCustomerTicketsByEventId(eventId)).thenReturn(ticketsPurchased);

        ActiveTicketException ex = assertThrows(ActiveTicketException.class, () -> {
            eventService.cancel(eventId);
        });

        assertTrue(ex.getMessage().contains("Tickets have already been sold"));
        verify(eventRepository).findById(eventId);
        verify(ticketManagerClient).checkTicketsByEventId(eventId);
        verify(ticketManagerClient).checkCustomerTicketsByEventId(eventId);
    }

    @Test
    @DisplayName("Should throw ClientComunicationError when TicketManagerClient throws FeignException")
    void cancelClientCommunicationError() {
        String eventId = "123";
        Event event = new Event("Event", LocalDateTime.now().plusDays(1), "01001-000");
        event.setStatus(EventStatus.ACTIVE);
        event.setId(eventId);

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(ticketManagerClient.checkTicketsByEventId(eventId)).thenThrow(FeignException.class);

        ClientComunicationError ex = assertThrows(ClientComunicationError.class, () -> {
            eventService.cancel(eventId);
        });

        assertEquals("Unable to communicate with ms-ticket-manager client. Try again later", ex.getMessage());
        verify(eventRepository).findById(eventId);
        verify(ticketManagerClient).checkTicketsByEventId(eventId);
    }
}