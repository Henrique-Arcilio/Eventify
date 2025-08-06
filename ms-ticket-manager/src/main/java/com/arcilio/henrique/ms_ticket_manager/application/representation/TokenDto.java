package com.arcilio.henrique.ms_ticket_manager.application.representation;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class TokenDto {
    private String username;
    private boolean authenticated;
    private Date created;
    private Date expiration;
    private String acessToken;
    private String refreshToken;
}
