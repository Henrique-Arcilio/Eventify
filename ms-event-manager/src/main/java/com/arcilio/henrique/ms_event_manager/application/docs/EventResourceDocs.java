package com.arcilio.henrique.ms_event_manager.application.docs;

import com.arcilio.henrique.ms_event_manager.application.exception.ErrorMessage;
import com.arcilio.henrique.ms_event_manager.application.representation.CreateEventDto;
import com.arcilio.henrique.ms_event_manager.application.representation.PageableDto;
import com.arcilio.henrique.ms_event_manager.application.representation.UpdateEventDto;
import com.arcilio.henrique.ms_event_manager.domain.model.Event;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface EventResourceDocs {

    String mediaTypeJson = MediaType.APPLICATION_JSON_VALUE;

    @Operation(summary = "Create a new Event",
            description = "Create a new event by parsing a JSON with event name, date and CEP",
            tags = {"Event"},
            responses = {
                @ApiResponse(
                        description = "Successfully created",
                        responseCode = "200",
                        content = @Content(mediaType = mediaTypeJson, schema = @Schema(implementation = Event.class))),
                @ApiResponse(
                        description = "Bad Request",
                        responseCode = "400",
                        content = @Content(mediaType = mediaTypeJson, schema = @Schema(implementation = ErrorMessage.class))),
                @ApiResponse(
                        description = "Unprocessable Entity",
                        responseCode = "422",
                        content = @Content(mediaType = mediaTypeJson, schema = @Schema(implementation = ErrorMessage.class))),
                @ApiResponse(
                        description = "Internal Server Error",
                        responseCode = "500",
                        content = @Content(mediaType = mediaTypeJson)
            )
    })
    ResponseEntity<Event> create(@Valid @RequestBody CreateEventDto dto);


    ResponseEntity<Event> getById(@PathVariable String id);

    ResponseEntity<PageableDto> getAll(Pageable pageable);

    ResponseEntity<PageableDto> getAllSorted(Pageable pageable);

    ResponseEntity<Void> update(@PathVariable String id, @Valid @RequestBody UpdateEventDto updateDto);

    ResponseEntity<Void> cancel(@PathVariable String id);
}
