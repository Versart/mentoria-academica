package com.versart.mentoria_academica.api.mapper;

import com.versart.mentoria_academica.api.model.EspecialidadeRequest;
import com.versart.mentoria_academica.api.model.EspecialidadeResponse;
import com.versart.mentoria_academica.domain.model.Especialidade;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EspecialidadeMapper {

    EspecialidadeMapper INSTANCE = Mappers.getMapper(EspecialidadeMapper.class);

    Especialidade toEspecialidade(EspecialidadeRequest especialidadeRequest);

    EspecialidadeResponse toEspecialidadeResponse(Especialidade especialidade);
}
