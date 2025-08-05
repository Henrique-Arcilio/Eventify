package com.arcilio.henrique.ms_event_manager.application.representation.mapper;

import com.arcilio.henrique.ms_event_manager.application.representation.PagableDto;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

public class PageableMapper {
    public static PagableDto toDto(Page page){
        return new ModelMapper().map(page, PagableDto.class);
    }
}
