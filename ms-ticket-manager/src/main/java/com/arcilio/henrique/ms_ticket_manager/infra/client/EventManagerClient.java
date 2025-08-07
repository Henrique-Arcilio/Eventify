package com.arcilio.henrique.ms_ticket_manager.infra.client;

import com.arcilio.henrique.ms_ticket_manager.application.representation.EventDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "${feign.data-resource.name}",  url = "${feign.data-resource.url}")
public interface EventManagerClient {
    @GetMapping("/get-event/{id}")
    EventDto getById(@PathVariable String id);

    @GetMapping("/get-all-events")
    List<EventDto> getAll(); 
}
