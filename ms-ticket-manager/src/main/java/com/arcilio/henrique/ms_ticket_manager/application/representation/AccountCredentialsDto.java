package com.arcilio.henrique.ms_ticket_manager.application.representation;

import lombok.Data;

import java.io.Serializable;

@Data
public class AccountCredentialsDto implements Serializable {

    private static final long serialVersion = 1L;
    private String username;
    private String password;
}
