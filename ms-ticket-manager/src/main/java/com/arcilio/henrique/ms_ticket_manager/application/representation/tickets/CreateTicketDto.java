package com.arcilio.henrique.ms_ticket_manager.application.representation.tickets;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class CreateTicketDto {
    @NotBlank(message = "The event id is required")
    private String eventId;
    @NotNull(message = "Total R$ is required")
    @PositiveOrZero(message = "R$ need to be a positive number")
    private Double brlTotalAmount;
}
