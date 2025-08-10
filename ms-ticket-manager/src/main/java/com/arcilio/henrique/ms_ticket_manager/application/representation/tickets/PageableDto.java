package com.arcilio.henrique.ms_ticket_manager.application.representation.tickets;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PageableDto {
    private List content;
    private boolean last;
    private boolean first;
    @JsonProperty("page")
    private int number;
    private int size;
    @JsonProperty("elementsInPage")
    private int numberOfElements;
    private int totalPages;
    private int totalElements;
}
