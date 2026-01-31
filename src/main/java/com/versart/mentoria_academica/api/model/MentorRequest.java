package com.versart.mentoria_academica.api.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.Set;

@Builder
public record MentorRequest(@NotBlank String codigo, @NotBlank String departamentoNome, @NotBlank String nomeCompleto, @NotNull @NotEmpty Set<String> linhasDePesquisa, String descricao, @Email @NotBlank String email) {
}
