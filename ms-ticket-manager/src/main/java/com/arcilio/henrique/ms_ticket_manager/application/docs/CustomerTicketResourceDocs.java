package com.arcilio.henrique.ms_ticket_manager.application.docs;

import com.arcilio.henrique.ms_ticket_manager.application.exception.ErrorMessage;
import com.arcilio.henrique.ms_ticket_manager.application.representation.tickets.GetCustomerTicketByIdDto;
import com.arcilio.henrique.ms_ticket_manager.application.representation.tickets.GetCustomerTicketsByEventDto;
import com.arcilio.henrique.ms_ticket_manager.application.representation.tickets.PageableDto;
import com.arcilio.henrique.ms_ticket_manager.domain.model.CustomerTicket;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    ResponseEntity<List<GetCustomerTicketsByEventDto>> getByEventId(@PathVariable String eventId);

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

    ResponseEntity<Void> cancel(@PathVariable String id, @AuthenticationPrincipal UserDetails userDetails);

    ResponseEntity<PageableDto> getAllForSale(Pageable pageable);
}
