package com.arcilio.henrique.ms_event_manager.application.representation;

import com.arcilio.henrique.ms_event_manager.domain.model.Event;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateEventDto {
    private String eventName;
    private LocalDateTime dateTime;
    private String cep;

    public Event fromDto(){
        return new Event(eventName, dateTime, cep);
    }
}
