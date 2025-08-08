package com.arcilio.henrique.ms_ticket_manager.application.representation.tickets;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class UpdateTicketDto {
    @NotBlank(message = "brlTotalAmount need cannot be blank")
    @PositiveOrZero(message = "R$ need to be a positive number")
    private Double brlTotalAmount;
}
