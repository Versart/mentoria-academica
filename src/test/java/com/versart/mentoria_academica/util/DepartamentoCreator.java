package com.versart.mentoria_academica.util;

import java.util.UUID;

import com.versart.mentoria_academica.domain.model.Departamento;

public class DepartamentoCreator {

    public static Departamento criarDepartamento() {
        return Departamento.builder().id(UUID.randomUUID()).nome("Computação").build();
    }
}
