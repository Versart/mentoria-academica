package com.versart.mentoria_academica.util;

import java.util.Set;
import java.util.UUID;

import com.versart.mentoria_academica.api.model.MentorRequest;
import com.versart.mentoria_academica.api.model.MentorResponse;
import com.versart.mentoria_academica.domain.model.Mentor;

public class MentorCreator {
    
    private static UUID uuid = UUID.randomUUID();

    public static Mentor criarMentorComId() {
        return Mentor.builder()
            .id(uuid)
            .codigo("21212")
            .departamento(DepartamentoCreator.criarDepartamento())
            .descricao("Professor de.....")
            .email("professor@email.com")
            .nomeCompleto("João da Silva")
            .linhasDePesquisa(Set.of(LinhaDePesquisaCreator.criarLinhaDePesquisaComId()))
            .build();
    }

    public static Mentor criarMentorSemId() {
        return Mentor.builder()
            .id(UUID.randomUUID())
            .codigo("21212")
            .departamento(DepartamentoCreator.criarDepartamento())
            .descricao("Professor de.....")
            .email("professor@email.com")
            .nomeCompleto("João da Silva")
            .build();
    }

    public static MentorRequest criarMentorRequest() {
        return MentorRequest.builder()
            .codigo("21212")
            .departamentoNome("Computação")
            .descricao("Professor de.....")
            .email("professor@email.com")
            .nomeCompleto("João da Silva")
            .linhasDePesquisa(Set.of(LinhaDePesquisaCreator.criarLinhaDePesquisaResponse().getNome()))
            .build();
    }

    public static MentorResponse criarMentorResponse() {
        return MentorResponse.builder()
            .id(uuid)
            .departamentoNome("Computação")
            .email("professor@email.com")
            .nomeCompleto("João da Silva")
            .linhasDePesquisa(Set.of("Robótica"))
            .disponivel(true)
            .build();
    }
}
