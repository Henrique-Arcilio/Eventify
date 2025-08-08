package com.arcilio.henrique.ms_ticket_manager.domain.model;

public enum TicketStatus {
    ACTIVE, CANCELLED;

    public boolean isActive(){
        return this == ACTIVE;
    }
}
