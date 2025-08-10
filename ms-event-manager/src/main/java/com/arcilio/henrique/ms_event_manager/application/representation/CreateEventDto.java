package com.arcilio.henrique.ms_event_manager.application.representation;

import com.arcilio.henrique.ms_event_manager.domain.model.Event;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateEventDto {

    @NotBlank(message = "The event must have a name")
    private String eventName;

    @Future(message = "The event date must be in the future.")
    @NotNull(message = "The event must have a date")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateTime;

    @Pattern(regexp = "\\d{5}-?\\d{3}",
            message = "CEP must contain 8 numeric digits, in the format 'xxxxx-xxx' or 'xxxxxxxx'.")
    private String cep;

    public Event fromDto(){
        return new Event(eventName, dateTime, cep);
    }
}
