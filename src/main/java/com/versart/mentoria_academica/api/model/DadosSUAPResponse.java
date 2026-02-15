package com.versart.mentoria_academica.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DadosSUAPResponse {

    @JsonProperty("nome_usual")
    private String nomeUsual;

    @JsonProperty("tipo_vinculo")
    private String tipoVinculo;

}
