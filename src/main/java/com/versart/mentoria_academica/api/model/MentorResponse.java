package com.versart.mentoria_academica.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MentorResponse {

    private UUID id;

    private String nomeCompleto;

    private String departamentoNome;

    private Set<String> linhasDePesquisa;
}
