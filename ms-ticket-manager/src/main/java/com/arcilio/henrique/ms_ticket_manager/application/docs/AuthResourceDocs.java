package com.arcilio.henrique.ms_ticket_manager.application.docs;

import com.arcilio.henrique.ms_ticket_manager.application.exception.ErrorMessage;
import com.arcilio.henrique.ms_ticket_manager.application.representation.account.AccountCredentialsDto;
import com.arcilio.henrique.ms_ticket_manager.application.representation.account.SignInCredentialsDto;
import com.arcilio.henrique.ms_ticket_manager.application.representation.account.TokenDto;
import com.arcilio.henrique.ms_ticket_manager.application.representation.tickets.GetCustomerTicketByIdDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthResourceDocs {

    String mediaTypeJson = MediaType.APPLICATION_JSON_VALUE;

    @Operation(summary = "Endpoint to sign in",
            description = "Return an authentication token JTW. (this endpoint has free access)",
            responses = {
                    @ApiResponse(
                            description = "Successfully Signed in",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = mediaTypeJson,
                                    array = @ArraySchema( schema = @Schema(implementation = TokenDto.class)))),
                    @ApiResponse(
                            description = "Unprocessable Entity",
                            responseCode = "422",
                            content = @Content(
                                    mediaType = mediaTypeJson, schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(
                            description = "forbidden: invalid credentials",
                            responseCode = "403",
                            content = @Content(
                                    mediaType = mediaTypeJson)),
                    @ApiResponse(
                            description = "Internal Server Error",
                            responseCode = "500",
                            content = @Content(mediaType = mediaTypeJson)
                    )
            })
    ResponseEntity<TokenDto> signIn(@Valid @RequestBody SignInCredentialsDto credentials);
    @Operation(summary = "Endpoint to create a new account",
            description = "Only customer role users can be created. (this endpoint has free access)",
            responses = {
                    @ApiResponse(
                            description = "User Successfully Created",
                            responseCode = "204",
                            content = @Content(
                                    mediaType = mediaTypeJson)),
                    @ApiResponse(
                            description = "Unprocessable Entity",
                            responseCode = "422",
                            content = @Content(
                                    mediaType = mediaTypeJson, schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(
                            description = "Username Already exists",
                            responseCode = "403",
                            content = @Content(
                                    mediaType = mediaTypeJson, schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(
                            description = "Internal Server Error",
                            responseCode = "500",
                            content = @Content(mediaType = mediaTypeJson)
                    )
            })
    ResponseEntity<Void> createUser(@Valid @RequestBody AccountCredentialsDto credentials);
}
