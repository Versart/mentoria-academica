package com.versart.mentoria_academica.api.exceptionhandler;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Problema {

    private int statusCode;

    private String messagem;

    private OffsetDateTime dataHora;

    private List<Campo> campos;
}
