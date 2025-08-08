package com.arcilio.henrique.ms_event_manager.application.representation;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateEventDto {


    private String eventName;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateTime;

    @Pattern(regexp = "\\d{5}-?\\d{3}",
            message = "Cep code must be in format {xxxxx-xxx} or {xxxxxxxx} (with exactly 8 numeric digits)")
    private String cep;


}
