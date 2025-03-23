package com.versart.mentoria_academica.api.mapper;

import com.versart.mentoria_academica.api.model.MentorRequest;
import com.versart.mentoria_academica.api.model.MentorResponse;
import com.versart.mentoria_academica.domain.exception.NaoEncontradoException;
import com.versart.mentoria_academica.domain.model.Departamento;
import com.versart.mentoria_academica.domain.model.LinhaDePesquisa;
import com.versart.mentoria_academica.domain.model.Mentor;
import com.versart.mentoria_academica.domain.repository.DepartamentoRepository;
import com.versart.mentoria_academica.domain.repository.LinhaDePesquisaRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface MentorMapper {

    MentorMapper INSTANCE = Mappers.getMapper(MentorMapper.class);



    @Mapping(target = "linhasDePesquisa", qualifiedByName ="mapLinhasDePesquisa" )
    @Mapping(target = "departamento", qualifiedByName ="mapDepartamento", source = "departamentoNome")
    Mentor toMentor(MentorRequest mentorRequest, @Context LinhaDePesquisaRepository linhaDePesquisaRepository, @Context DepartamentoRepository departamentoRepository);

    Mentor toMentor(Mentor mentor, @Context LinhaDePesquisaRepository linhaDePesquisaRepository, @Context DepartamentoRepository departamentoRepository);

    @Mapping(target ="departamentoNome", source = "departamento.nome")
    MentorResponse toMentorResponse(Mentor mentor);

    @Named("mapDepartamento")
    default Departamento mapDepartamento(String departamentoNome, @Context DepartamentoRepository departamentoRepository) {
        return departamentoRepository.findByNome(departamentoNome).orElseThrow(
                () -> new NaoEncontradoException("Departamento não encontrado")
        );
    }

    @Named("mapLinhasDePesquisa")
    default Set<LinhaDePesquisa> mapLinhasDePesquisa(Set<String> especialidades, @Context LinhaDePesquisaRepository linhaDePesquisaRepository) {
        return especialidades.stream().map(
                especialidade -> {
                    return linhaDePesquisaRepository.findByNome(especialidade).orElseThrow(
                            () -> new NaoEncontradoException("Linha de Pesquisa não encontrada"));

                }
        ).collect(Collectors.toSet());
    }

    Set<MentorResponse> toMentorResponseSet(Set<Mentor> mentores);
}
