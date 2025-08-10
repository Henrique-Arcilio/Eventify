package com.arcilio.henrique.ms_event_manager.application.docs;

import com.arcilio.henrique.ms_event_manager.application.exception.ErrorMessage;
import com.arcilio.henrique.ms_event_manager.application.representation.CreateEventDto;
import com.arcilio.henrique.ms_event_manager.application.representation.PageableDto;
import com.arcilio.henrique.ms_event_manager.application.representation.UpdateEventDto;
import com.arcilio.henrique.ms_event_manager.domain.model.Event;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
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

    @Operation(summary = "Find an event by the given id",
            tags = {"Event"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(mediaType = mediaTypeJson, schema = @Schema(implementation = Event.class))),
                    @ApiResponse(
                            description = "Not Found",
                            responseCode = "404",
                            content = @Content(mediaType = mediaTypeJson, schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(
                            description = "Conflict: Event has been cancelled",
                            responseCode = "409",
                            content = @Content(mediaType = mediaTypeJson, schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(
                            description = "Internal Server Error",
                            responseCode = "500",
                            content = @Content(mediaType = mediaTypeJson)
                    )
            })
    ResponseEntity<Event> getById(@PathVariable
                                  @Parameter(description = "The event id") String id);

    @Operation(summary = "Find all the events",
            description = "Find all events (even cancelled events)",
            tags = {"Event"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(mediaType = mediaTypeJson, schema = @Schema(implementation = PageableDto.class))),
                    @ApiResponse(
                            description = "Internal Server Error",
                            responseCode = "500",
                            content = @Content(mediaType = mediaTypeJson)
                    )
            })
    ResponseEntity<PageableDto> getAll(Pageable pageable);

    @Operation(summary = "Find all the events sorted",
            description = "Find all events but in alphabetical order",
            tags = {"Event"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(mediaType = mediaTypeJson, schema = @Schema(implementation = PageableDto.class))),
                    @ApiResponse(
                            description = "Internal Server Error",
                            responseCode = "500",
                            content = @Content(mediaType = mediaTypeJson)
                    )

            })
    ResponseEntity<PageableDto> getAllSorted(Pageable pageable);

    @Operation(summary = "Update event name, CEP or date",
            description = "It's possible to choose which attribute change. " +
                    "The update also update the event tickets information " +
                    "(CEP updates address info automatically based on VIACEP API).",
            tags = {"Event"},
            responses = {
                    @ApiResponse(
                            description = "Successfully Updated",
                            responseCode = "200",
                            content = @Content(mediaType = mediaTypeJson, schema = @Schema(implementation = Event.class))),
                    @ApiResponse(
                            description = "Not Found",
                            responseCode = "404",
                            content = @Content(mediaType = mediaTypeJson, schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(
                            description = "Unprocessable entity while trying to update data",
                            responseCode = "422",
                            content = @Content(mediaType = mediaTypeJson, schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(
                            description = "Service Unavailable: Failed to comunicate with ticket manager",
                            responseCode = "503",
                            content = @Content(mediaType = mediaTypeJson, schema = @Schema(implementation = ErrorMessage.class))),

                    @ApiResponse(
                            description = "Internal Server Error",
                            responseCode = "500",
                            content = @Content(mediaType = mediaTypeJson)
                    )
            })
    ResponseEntity<Void> update(@PathVariable
                                @Parameter(description = "The id of the event to update ") String id,
                                @Valid @RequestBody UpdateEventDto updateDto);


    ResponseEntity<Void> cancel(@PathVariable String id);
}
