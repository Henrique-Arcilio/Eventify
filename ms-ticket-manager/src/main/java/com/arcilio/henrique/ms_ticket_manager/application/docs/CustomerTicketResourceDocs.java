package com.arcilio.henrique.ms_ticket_manager.application.docs;

import com.arcilio.henrique.ms_ticket_manager.application.exception.ErrorMessage;
import com.arcilio.henrique.ms_ticket_manager.application.representation.tickets.GetCustomerTicketByIdDto;
import com.arcilio.henrique.ms_ticket_manager.application.representation.tickets.GetCustomerTicketsByEventDto;
import com.arcilio.henrique.ms_ticket_manager.application.representation.tickets.GetTicketByEventDto;
import com.arcilio.henrique.ms_ticket_manager.application.representation.tickets.PageableDto;
import com.arcilio.henrique.ms_ticket_manager.domain.model.CustomerTicket;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface CustomerTicketResourceDocs {

    String mediaTypeJson = MediaType.APPLICATION_JSON_VALUE;

    @Operation(summary = "Get all Tickets purchased in an Event id",
            description = "Only return tickets with ACTIVE status",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = mediaTypeJson,
                                    array = @ArraySchema( schema = @Schema(implementation = GetCustomerTicketByIdDto.class)))),
                    @ApiResponse(
                            description = "Internal Server Error",
                            responseCode = "500",
                            content = @Content(mediaType = mediaTypeJson)
                    )
            })
    ResponseEntity<List<GetCustomerTicketsByEventDto>> getByEventId(@PathVariable
                                                                    @Parameter(description = "Id of the event to filter by") String eventId);

    ResponseEntity<GetCustomerTicketByIdDto> getById(@PathVariable String id);

    ResponseEntity<PageableDto> findAll(Pageable pageable, @AuthenticationPrincipal UserDetails userDetails);


    @Operation(summary = "Create a new CustomerTicket based on the provided ticket-for-sale ID",
            responses = {
                    @ApiResponse(
                            description = "Successfully created",
                            responseCode = "200",
                            content = @Content(mediaType = mediaTypeJson, schema = @Schema(implementation = CustomerTicket.class))),
                    @ApiResponse(
                            description = "Not Found",
                            responseCode = "404",
                            content = @Content(mediaType = mediaTypeJson, schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(
                            description = "Internal Server Error",
                            responseCode = "500",
                            content = @Content(mediaType = mediaTypeJson)
                    )
            })
    ResponseEntity<CustomerTicket> buy(@PathVariable @Parameter(description = "Id of the desired ticket") String id,
                                       @AuthenticationPrincipal UserDetails userDetails);

    @Operation(summary = "Customer can cancel a ticket by its id",
            responses = {
                    @ApiResponse(
                            description = "Successfully Cancelled",
                            responseCode = "204",
                            content = @Content(mediaType = mediaTypeJson)),
                    @ApiResponse(
                            description = "Not Found",
                            responseCode = "404",
                            content = @Content(mediaType = mediaTypeJson, schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(
                            description = "Internal Server Error",
                            responseCode = "500",
                            content = @Content(mediaType = mediaTypeJson)
                    )
            })
    ResponseEntity<Void> cancel(@PathVariable @Parameter(description = "Id of the customer ticekt to cancel") String id,
                                @AuthenticationPrincipal UserDetails userDetails);


    ResponseEntity<PageableDto> getAllForSale(Pageable pageable);
}
