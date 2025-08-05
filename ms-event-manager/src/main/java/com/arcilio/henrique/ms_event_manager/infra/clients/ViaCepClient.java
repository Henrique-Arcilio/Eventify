package com.arcilio.henrique.ms_event_manager.infra.clients;

import com.arcilio.henrique.ms_event_manager.application.representation.ViaCepAdressDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "${feign.data-resource.name}",  url = "${feign.data-resource.url}")
public interface ViaCepClient {
    @GetMapping("{cep}/json")
    ViaCepAdressDto getAdressInformation(@PathVariable String cep);
}
