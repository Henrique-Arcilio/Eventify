package com.arcilio.henrique.ms_ticket_manager.application.representation.tickets;

import com.arcilio.henrique.ms_ticket_manager.application.representation.EventDto;
import lombok.Data;

@Data
public class GetTicketForSaleByIdDto {
    private EventDto event;
    private Double BRLTotalAmount;
    private Double USDTotalAmount;
}
