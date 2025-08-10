package com.arcilio.henrique.ms_event_manager.infra.clients.ticket;

import com.arcilio.henrique.ms_event_manager.application.representation.CheckTicketDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "${feign.ticket-manager.name}", url= "${feign.ticket-manager.url}")
public interface TicketManagerClient {
    @GetMapping("/tickets/events/{eventId}")
    List<CheckTicketDto> checkTicketsByEventId(@PathVariable String eventId);

    @GetMapping("/customer-tickets/events/{eventId}")
    List<CheckTicketDto> checkCustomerTicketsByEventId(@PathVariable String eventId);

    @PatchMapping("tickets/{eventId}/sync")
    void syncEventUpdates(@PathVariable String eventId);
}
