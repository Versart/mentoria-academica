package com.versart.mentoria_academica.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DepartamentoResponse {

    private UUID id;
    private String nome;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<MentorResponse> mentores;
}
