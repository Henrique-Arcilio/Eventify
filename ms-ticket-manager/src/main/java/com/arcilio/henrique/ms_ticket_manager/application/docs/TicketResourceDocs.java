package com.arcilio.henrique.ms_ticket_manager.application.docs;


import com.arcilio.henrique.ms_ticket_manager.application.exception.ErrorMessage;
import com.arcilio.henrique.ms_ticket_manager.application.representation.tickets.*;
import com.arcilio.henrique.ms_ticket_manager.domain.model.Ticket;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface TicketResourceDocs {

    String mediaTypeJson = MediaType.APPLICATION_JSON_VALUE;

    @Operation(summary = "Create a new Ticket for Sale",
            description = "Create a ticket for an event and put it for sale." +
                    "The usd value is calculated based in an fictional fix value: brlTotalAmount * 5",
            tags = {"Ticket, sales"},
            responses = {
                    @ApiResponse(
                            description = "Successfully created",
                            responseCode = "200",
                            content = @Content(mediaType = mediaTypeJson, schema = @Schema(implementation = Ticket.class))),
                    @ApiResponse(
                            description = "Not Found",
                            responseCode = "404",
                            content = @Content(mediaType = mediaTypeJson, schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(
                            description = "Conflict: Event id references a cancelled event",
                            responseCode = "409",
                            content = @Content(mediaType = mediaTypeJson, schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(
                            description = "Unprocessable Entity",
                            responseCode = "422",
                            content = @Content(mediaType = mediaTypeJson, schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(
                            description = "Unavailable Service: Could not communicate with Event manager",
                            responseCode = "503",
                            content = @Content(mediaType = mediaTypeJson, schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(
                            description = "Internal Server Error",
                            responseCode = "500",
                            content = @Content(mediaType = mediaTypeJson)
                    )
            })
    ResponseEntity<Ticket> create(@Valid @RequestBody CreateTicketDto dto);

    ResponseEntity<List<GetTicketByEventDto>> getByEventId(@PathVariable String eventId);

    ResponseEntity<GetTicketByIdDto> getById(@PathVariable String id);

    public ResponseEntity<PageableDto> getAll(Pageable pageable);

    ResponseEntity<Void> update(@PathVariable String id, @Valid @RequestBody UpdateTicketDto updateDto);

    ResponseEntity<Void> cancel(@PathVariable String id);

    ResponseEntity<Void> syncEventUpdates(@PathVariable String eventId);
}
