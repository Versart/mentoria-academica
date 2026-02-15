package com.versart.mentoria_academica.util;

import java.util.UUID;

import com.versart.mentoria_academica.api.model.LinhaDePesquisaRequest;
import com.versart.mentoria_academica.api.model.LinhaDePesquisaResponse;
import com.versart.mentoria_academica.domain.model.LinhaDePesquisa;

public class LinhaDePesquisaCreator {

    private static UUID uuid = UUID.randomUUID();

    public static LinhaDePesquisa criarLinhaDePesquisaComId() {
        return LinhaDePesquisa.builder()
            .id(uuid)
            .nome("Robótica")
            .build();
    }
    
    public static LinhaDePesquisaRequest criarLinhaDePesquisaRequest() {
        return LinhaDePesquisaRequest.builder()
            .nome("Robótica")
            .build();
    }

    public static LinhaDePesquisaResponse criarLinhaDePesquisaResponse() {
        return LinhaDePesquisaResponse.builder()
            .nome("Robótica")
            .build();
    }
}
