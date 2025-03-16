package com.versart.mentoria_academica.api.mapper;

import com.versart.mentoria_academica.api.model.DepartamentoRequest;
import com.versart.mentoria_academica.domain.model.Departamento;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DepartamentoMapper {

    DepartamentoMapper INSTANCE = Mappers.getMapper(DepartamentoMapper.class);

    Departamento toDepartamento(DepartamentoRequest departamentoRequest);



}
