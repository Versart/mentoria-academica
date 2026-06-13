package com.versart.mentoria_academica.util;

import java.util.Set;
import java.util.UUID;

import com.versart.mentoria_academica.api.model.DepartamentoRequest;
import com.versart.mentoria_academica.api.model.DepartamentoResponse;
import com.versart.mentoria_academica.domain.model.Departamento;

public class DepartamentoCreator {

    private static UUID uuid = UUID.randomUUID();

    public static Departamento criarDepartamentoComId() {
        return Departamento.builder().id(uuid).nome("Computação").build();
    }

   public static Departamento criarDepartamentoSemId() {
        return Departamento.builder().nome("Computação").build();
   }

   public static DepartamentoResponse criarDepartamentoResponse() {
        return DepartamentoResponse.builder()
            .id(uuid)
            .nome("Computacao")
            .mentores(Set.of(MentorCreator.criarMentorResponse()))
            .build();  
   }

   public static DepartamentoRequest criarDepartamentoRequest() {
        return DepartamentoRequest.builder()
            .nome("Computacao")
            .build();
   }
}
