package com.versart.mentoria_academica.api.mapper;

import com.versart.mentoria_academica.api.model.LinhaDePesquisaRequest;
import com.versart.mentoria_academica.api.model.LinhaDePesquisaResponse;
import com.versart.mentoria_academica.domain.model.LinhaDePesquisa;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LinhaDePesquisaMapper {

    LinhaDePesquisaMapper INSTANCE = Mappers.getMapper(LinhaDePesquisaMapper.class);

    LinhaDePesquisa toLinhaDePesquisa(LinhaDePesquisaRequest linhaDePesquisaRequest);

    LinhaDePesquisaResponse toLinhaDePesquisaResponse(LinhaDePesquisa especialidade);
}
