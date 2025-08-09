package com.arcilio.henrique.ms_event_manager.domain.model;

public enum EventStatus {
    ACTIVE, CANCELLED;

    public boolean isActive(){
        return this == ACTIVE;
    }
}
