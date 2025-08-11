package com.arcilio.henrique.ms_ticket_manager.application.docs;


import com.arcilio.henrique.ms_ticket_manager.application.exception.ErrorMessage;
import com.arcilio.henrique.ms_ticket_manager.application.representation.tickets.*;
import com.arcilio.henrique.ms_ticket_manager.domain.model.Ticket;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
                    "The usd value is calculated based in a fictional fix value: brlTotalAmount * 5",
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

    @Operation(summary = "Get all Tickets for sale related to an Event",
            description = "Only return tickets for sale with ACTIVE status",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = mediaTypeJson,
                                    array = @ArraySchema( schema = @Schema(implementation = GetTicketByEventDto.class)))),
                    @ApiResponse(
                            description = "Internal Server Error",
                            responseCode = "500",
                            content = @Content(mediaType = mediaTypeJson)
                    )
            })
    ResponseEntity<List<GetTicketByEventDto>> getByEventId(@PathVariable
                                                           @Parameter(description = "Id of the event to filter by") String eventId);

    @Operation(summary = "Find an ticket for sale using the given id",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(mediaType = mediaTypeJson, schema = @Schema(implementation = GetTicketByIdDto.class))),
                    @ApiResponse(
                            description = "Not Found",
                            responseCode = "200",
                            content = @Content(mediaType = mediaTypeJson, schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(
                            description = "Internal Server Error",
                            responseCode = "500",
                            content = @Content(mediaType = mediaTypeJson)
                    )
            })
    ResponseEntity<GetTicketByIdDto> getById(@PathVariable @Parameter(description = "Id of the ticket for sale") String id);

    @Operation(summary = "Get all Tickets for sale",
            description = "Get all tickets for sale (including the cancelled ones)",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = mediaTypeJson,
                                    array = @ArraySchema( schema = @Schema(implementation = PageableDto.class)))),
                    @ApiResponse(
                            description = "Internal Server Error",
                            responseCode = "500",
                            content = @Content(mediaType = mediaTypeJson)
                    )
            })
    ResponseEntity<PageableDto> getAll(Pageable pageable);

    @Operation(summary = "Update a ticket for sale",
            description = "Update only the ticket price",
            responses = {
                    @ApiResponse(
                            description = "Successfully updated",
                            responseCode = "204",
                            content = @Content(mediaType = mediaTypeJson)),
                    @ApiResponse(
                            description = "Not Found",
                            responseCode = "404",
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
    ResponseEntity<Void> update(@PathVariable
                                @Parameter(description = "Id of the ticket to update")
                                String id, @Valid @RequestBody UpdateTicketDto updateDto);


    @Operation(summary = "Cancel the sale of a ticket by id",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(mediaType = mediaTypeJson)),
                    @ApiResponse(
                            description = "Not Found",
                            responseCode = "404",
                            content = @Content(mediaType = mediaTypeJson,
                                    schema =  @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(
                            description = "Internal Server Error",
                            responseCode = "500",
                            content = @Content(mediaType = mediaTypeJson)
                    )
            })
    ResponseEntity<Void> cancel(@PathVariable @Parameter(description = "Id of the ticket to cancel") String id);

    @Hidden
    ResponseEntity<Void> syncEventUpdates(@PathVariable String eventId);
}
