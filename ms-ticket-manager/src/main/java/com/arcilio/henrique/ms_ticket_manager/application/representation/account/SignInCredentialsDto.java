package com.arcilio.henrique.ms_ticket_manager.application.representation.account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
public class SignInCredentialsDto implements Serializable {
    private static final long serialVersion = 1L;
    @NotBlank(message = "User e-mail is required")
    @Email
    private String username;

    @NotBlank(message = "User password is required")
    @Size(min = 4, message = "need at least 4 characters")
    private String password;
}
