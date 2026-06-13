package com.versart.mentoria_academica.api.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record DepartamentoRequest(@NotBlank String nome) {
}
