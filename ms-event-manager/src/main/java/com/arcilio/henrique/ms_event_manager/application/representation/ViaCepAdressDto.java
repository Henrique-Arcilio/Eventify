package com.arcilio.henrique.ms_event_manager.application.representation;

import lombok.Data;

@Data
public class ViaCepAdressDto {
    private String logradouro;
    private String bairro;
    private String localidade;
    private String uf;
}
