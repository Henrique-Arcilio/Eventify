package com.arcilio.henrique.ms_ticket_manager.application.representation.mapper;

import com.arcilio.henrique.ms_ticket_manager.application.representation.tickets.PageableDto;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

public class PageableMapper {
    public static PageableDto toDto(Page page){
        return new ModelMapper().map(page, PageableDto.class);
    }

}
