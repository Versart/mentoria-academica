package com.versart.mentoria_academica.api.model;

import jakarta.validation.constraints.NotBlank;

public record EspecialidadeRequest (@NotBlank String nome){
}
