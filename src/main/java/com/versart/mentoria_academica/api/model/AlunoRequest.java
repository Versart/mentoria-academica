package com.versart.mentoria_academica.api.model;

import jakarta.validation.constraints.NotBlank;

public record AlunoRequest(@NotBlank String codigo, @NotBlank String curso, @NotBlank String nomeCompleto) {
}
