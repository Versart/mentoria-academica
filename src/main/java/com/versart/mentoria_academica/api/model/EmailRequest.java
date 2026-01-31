package com.versart.mentoria_academica.api.model;

import jakarta.validation.constraints.NotBlank;

public record EmailRequest(@NotBlank String resumo, @NotBlank String emailProfessor) {
    
}
