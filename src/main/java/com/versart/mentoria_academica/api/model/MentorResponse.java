package com.versart.mentoria_academica.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MentorResponse {

    private UUID id;

    private String nomeCompleto;

    private String departamentoNome;

    private Set<String> linhasDePesquisa;

    private String email;

    private Boolean disponivel;

    private String descricao;
}
