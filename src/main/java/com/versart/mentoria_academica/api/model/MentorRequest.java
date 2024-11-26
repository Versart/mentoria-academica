package com.versart.mentoria_academica.api.model;

import jakarta.validation.constraints.NotBlank;

public record MentorRequest(@NotBlank String codigo, @NotBlank String departamento, @NotBlank String nomeCompleto) {
}
