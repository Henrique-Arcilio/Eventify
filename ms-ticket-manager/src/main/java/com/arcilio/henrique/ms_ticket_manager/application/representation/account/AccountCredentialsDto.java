package com.arcilio.henrique.ms_ticket_manager.application.representation.account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

import java.io.Serializable;

@Data
public class AccountCredentialsDto implements Serializable {

    private static final long serialVersion = 1L;

    @NotBlank(message = "User e-mail is required")
    @Email(message = "please insert a valid email")
    private String username;

    @NotBlank(message = "User password is required")
    @Size(min = 4, message = "need at least 4 characters")
    private String password;

    @NotBlank(message = "CPF is required")
    @CPF(message = "Need to be in the correct format XXX.XXX.XXX-XX or XXXXXXXXXXX")
    private String cpf;

    @NotBlank(message = "User name is required")
    private String fullname;

}
